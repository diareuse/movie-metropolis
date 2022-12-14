package movie.metropolis.app.feature.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.os.bundleOf
import com.google.android.gms.location.LocationServices

@Composable
fun rememberLocation(
    onPermissionRequested: suspend (Array<String>) -> Boolean
): State<Location?> {
    val context = LocalContext.current
    val provider = remember { LocationServices.getFusedLocationProviderClient(context) }
    val snapshotState = rememberSaveable(key = "location", stateSaver = LocationSaver) {
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

private val LocationSaver = object : Saver<Location?, Bundle> {
    override fun restore(value: Bundle): Location? {
        val latitude = value.getDouble("latitude", Double.MAX_VALUE)
        val longitude = value.getDouble("longitude", Double.MAX_VALUE)
        if (latitude == Double.MAX_VALUE || longitude == Double.MAX_VALUE) return null
        return Location(null).apply {
            this.latitude = latitude
            this.longitude = longitude
        }
    }

    override fun SaverScope.save(value: Location?): Bundle? {
        return value?.let { value ->
            bundleOf(
                "latitude" to value.latitude,
                "longitude" to value.longitude
            )
        }
    }
}