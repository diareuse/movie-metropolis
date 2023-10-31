@file:OptIn(ExperimentalPermissionsApi::class)

package movie.metropolis.app.feature.location

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.platform.*
import androidx.core.os.bundleOf
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("MissingPermission")
@Composable
fun rememberLocation(
    state: MultiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
): State<Location?> {
    val context = LocalContext.current
    val provider = remember { LocationServices.getFusedLocationProviderClient(context) }
    val snapshotState = rememberSaveable(key = "location", stateSaver = LocationSaver) {
        mutableStateOf(null as Location?)
    }

    LaunchedEffect(state.allPermissionsGranted) {
        if (!state.allPermissionsGranted) return@LaunchedEffect
        provider.requestLocationUpdates().collectLatest {
            snapshotState.value = it
        }
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
        return value?.let {
            bundleOf(
                "latitude" to value.latitude,
                "longitude" to value.longitude
            )
        }
    }
}