package movie.metropolis.app.feature.play

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions.defaultOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import kotlinx.coroutines.tasks.await
import movie.metropolis.app.R
import movie.metropolis.app.util.isConnectionMetered
import movie.style.AnticipateOvershootEasing
import movie.style.theme.Theme
import movie.style.util.findActivityOrNull

@Composable
fun PlayUpdate(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
    val state by rememberAppUpdateState()
    val progress = when (val s = state) {
        is AppUpdateState.Downloading -> s.progress
        AppUpdateState.None -> -1f
    }
    AnimatedVisibility(
        modifier = modifier,
        visible = progress >= 0f,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(
                easing = AnticipateOvershootEasing,
                durationMillis = 600
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(
                easing = AnticipateOvershootEasing,
                durationMillis = 600
            )
        )
    ) {
        PlayUpdateInProgress(
            progress = progress,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun PlayUpdateInProgress(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val progressValue by animateFloatAsState(progress)
    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Min)
            .shadow(
                elevation = 16.dp,
                shape = Theme.container.card,
                ambientColor = Theme.color.container.primary,
                spotColor = Theme.color.container.primary,
                clip = false
            )
            .background(Theme.color.container.background, Theme.container.card)
            .clip(Theme.container.card),
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = progressValue,
            color = Theme.color.container.primary,
            backgroundColor = Theme.color.container.primary.copy(alpha = .4f)
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.downloading_update),
            style = Theme.textStyle.caption,
            color = Theme.color.content.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        PlayUpdateInProgress(progress = .3f, modifier = Modifier.padding(24.dp))
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    Theme {
        PlayUpdateInProgress(progress = .3f, modifier = Modifier.padding(24.dp))
    }
}


@Composable
fun rememberAppUpdateState(): State<AppUpdateState> {
    val context = LocalContext.current
    val state = remember {
        mutableStateOf<AppUpdateState>(AppUpdateState.None)
    }
    val activity = remember(context) { context.findActivityOrNull() } ?: return state
    val manager = remember(context) {
        AppUpdateManagerFactory.create(context)
    }

    fun AppUpdateInfo.startUpdate(@AppUpdateType type: Int) = when (type) {
        IMMEDIATE -> manager.startUpdateFlowForResult(this, activity, defaultOptions(IMMEDIATE), 1)
        FLEXIBLE -> when (context.isConnectionMetered) {
            true -> false
            else -> manager.startUpdateFlowForResult(this, activity, defaultOptions(FLEXIBLE), 1)
        }

        else -> false
    }

    DisposableEffect(manager) {
        val listener = InstallStateUpdatedListener {
            when (it.installStatus()) {
                InstallStatus.DOWNLOADED -> manager.completeUpdate()
                InstallStatus.DOWNLOADING -> state.value = AppUpdateState.Downloading(
                    progress = 1f * it.bytesDownloaded() / it.totalBytesToDownload()
                )
            }
        }
        manager.registerListener(listener)
        onDispose {
            manager.unregisterListener(listener)
        }
    }
    LaunchedEffect(manager) {
        val info = manager.appUpdateInfo.runCatching { await() }.getOrNull()
        when (info?.updateAvailability()) {
            UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS ->
                manager.startUpdateFlowForResult(info, activity, defaultOptions(IMMEDIATE), 1)

            UpdateAvailability.UPDATE_AVAILABLE -> when {
                info.isFlexibleUpdateAllowed -> info.startUpdate(FLEXIBLE)
                info.isImmediateUpdateAllowed -> info.startUpdate(IMMEDIATE)
            }

            UpdateAvailability.UPDATE_NOT_AVAILABLE ->
                state.value = AppUpdateState.None
        }
    }
    return state
}

sealed class AppUpdateState {

    data class Downloading(
        val progress: Float
    ) : AppUpdateState()

    object None : AppUpdateState()

}