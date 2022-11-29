package movie.metropolis.app.feature.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.checkSelfPermission
import com.google.android.gms.location.LocationServices

@Composable
fun rememberLocation(
    onPermissionRequested: suspend (Array<String>) -> Boolean
): State<Location?> {
    val context = LocalContext.current
    val provider = remember { LocationServices.getFusedLocationProviderClient(context) }
    val snapshotState = remember {
        mutableStateOf(null as Location?)
    }
    LaunchedEffect(onPermissionRequested) {
        if (checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            if (!onPermissionRequested(arrayOf(ACCESS_COARSE_LOCATION))) {
                return@LaunchedEffect
            }
        }
        snapshotState.value = kotlin
            .runCatching { provider.getLastLocation(context).let(::requireNotNull) }
            .recoverCatching { provider.getCurrentLocation(context).let(::requireNotNull) }
            .getOrNull()
    }
    return snapshotState
}