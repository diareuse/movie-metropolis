package movie.metropolis.app.screen.booking.component

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import movie.metropolis.app.ActivityActions
import movie.metropolis.app.LocalActivityActions
import movie.metropolis.app.R
import movie.metropolis.app.presentation.share.TicketRepresentation
import movie.metropolis.app.screen.reader.BarcodeReader
import movie.metropolis.app.util.register
import movie.metropolis.app.util.toBitmap
import movie.style.AppButton
import movie.style.AppDialog
import movie.style.theme.Theme

@SuppressLint("MissingPermission")
@Composable
fun ReaderDialog(
    isVisible: Boolean,
    onVisibilityChanged: (Boolean) -> Unit,
    onTicketRead: (TicketRepresentation) -> Unit,
    actions: ActivityActions = LocalActivityActions.current
) {
    var hasPermission by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (!isVisible) return@LaunchedEffect
        hasPermission = actions.requestPermissions(arrayOf(Manifest.permission.CAMERA))
    }
    AppDialog(
        isVisible = isVisible && hasPermission,
        onVisibilityChanged = onVisibilityChanged
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                shape = Theme.container.card,
                color = Theme.color.container.background,
                shadowElevation = 32.dp
            ) {
                BarcodeReader(
                    modifier = Modifier.fillMaxSize(),
                    format = BarcodeFormat.PDF_417,
                    onBarcodeRead = {
                        onVisibilityChanged(false)
                        onTicketRead(TicketRepresentation.Text(it))
                    }
                )
            }
            val owner = LocalActivityResultRegistryOwner.current?.activityResultRegistry
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            if (owner != null) AppButton(
                onClick = {
                    scope.launch(Dispatchers.Default) {
                        val image = owner.register("image", GetContent(), "image/*")
                            ?.toBitmap(context)
                            ?.let(TicketRepresentation::Image) ?: return@launch
                        withContext(Dispatchers.Main.immediate) {
                            onTicketRead(image)
                            onVisibilityChanged(false)
                        }
                    }
                },
                elevation = 16.dp
            ) {
                Text(stringResource(R.string.from_file))
            }
        }
    }
}