package movie.metropolis.app.feature.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await

@Throws
@RequiresPermission(ACCESS_COARSE_LOCATION)
suspend fun FusedLocationProviderClient.awaitLastLocation(): Location? {
    return lastLocation.await()
}

@Throws
@RequiresPermission(ACCESS_COARSE_LOCATION)
suspend fun FusedLocationProviderClient.awaitCurrentLocation(): Location? {
    return getCurrentLocation(Priority.PRIORITY_PASSIVE, null).await()
}

@RequiresPermission(ACCESS_COARSE_LOCATION)
fun FusedLocationProviderClient.requestLocationUpdates(
    request: LocationRequest
): Flow<LocationResult?> = callbackFlow {
    val listener = object : LocationCallback() {
        override fun onLocationAvailability(availability: LocationAvailability) {
            if (!availability.isLocationAvailable)
                trySend(null)
        }

        override fun onLocationResult(result: LocationResult) {
            trySend(result)
        }
    }
    requestLocationUpdates(request, listener, null)
    awaitClose {
        removeLocationUpdates(listener)
    }
}

@RequiresPermission(ACCESS_COARSE_LOCATION)
fun FusedLocationProviderClient.requestLocationUpdates(): Flow<Location?> =
    LocationRequest.Builder(Priority.PRIORITY_PASSIVE, 5000)
        .setWaitForAccurateLocation(false)
        .build()
        .let { requestLocationUpdates(it) }
        .map { it?.lastLocation }
        .onStart {
            kotlin.runCatching { awaitLastLocation() }
                .recoverCatching { awaitCurrentLocation() }
                .onSuccess { emit(it) }
        }
        .distinctUntilChanged()