package movie.metropolis.app.screen.profile.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.profile.component.ActionsWithProgressParameter.Data
import movie.style.AppButton
import movie.style.layout.PreviewLayout

@Composable
fun ActionsWithProgress(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    error: Boolean = false,
    errorAction: @Composable () -> Unit = {},
    progressIndicator: @Composable () -> Unit = {
        CircularProgressIndicator()
    },
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(loading) {
            Box(Modifier.scale(.5f)) { progressIndicator() }
        }
        AnimatedVisibility(error) {
            errorAction()
        }
        content()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ActionsWithProgressPreview(
    @PreviewParameter(ActionsWithProgressParameter::class)
    parameter: Data
) = PreviewLayout {
    ActionsWithProgress(
        loading = parameter.loading,
        error = parameter.error,
        errorAction = {
            AppButton(onClick = {}) {
                Text("PANIC!")
            }
        }
    ) {
        AppButton(onClick = {}, enabled = !parameter.loading) {
            Text("Do something")
        }
    }
}

private class ActionsWithProgressParameter : CollectionPreviewParameterProvider<Data>(
    listOf(
        Data(loading = true, error = true),
        Data(loading = true, error = false),
        Data(loading = false, error = true),
    )
) {
    data class Data(
        val loading: Boolean,
        val error: Boolean
    )
}