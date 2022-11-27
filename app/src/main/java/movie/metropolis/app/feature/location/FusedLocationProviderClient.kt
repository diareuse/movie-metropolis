package movie.metropolis.app.feature.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import androidx.core.app.ActivityCompat.checkSelfPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import movie.metropolis.app.model.LocationSnapshot
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

sealed class LocationException : RuntimeException() {
    class PermissionMissing : LocationException()
    class NotAvailable(override val cause: Throwable?) : LocationException()
}

@Throws(LocationException::class)
suspend fun FusedLocationProviderClient.getLastLocation(
    context: Context
) = suspendCancellableCoroutine { cont ->
    if (checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
        cont.resumeWithException(LocationException.PermissionMissing())
        return@suspendCancellableCoroutine
    }
    lastLocation.addOnCompleteListener {
        when (it.isSuccessful) {
            true -> cont.resume(LocationSnapshot(it.result))
            else -> cont.resumeWithException(LocationException.NotAvailable(it.exception))
        }
    }
}

@Throws(LocationException::class)
suspend fun FusedLocationProviderClient.getCurrentLocation(
    context: Context
) = suspendCancellableCoroutine { cont ->
    if (checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
        cont.resumeWithException(LocationException.PermissionMissing())
        return@suspendCancellableCoroutine
    }
    getCurrentLocation(Priority.PRIORITY_LOW_POWER, null).addOnCompleteListener {
        when (it.isSuccessful) {
            true -> cont.resume(LocationSnapshot(it.result))
            else -> cont.resumeWithException(LocationException.NotAvailable(it.exception))
        }
    }
}

private fun LocationSnapshot(location: Location?): LocationSnapshot? {
    location ?: return null
    return LocationSnapshot(location.latitude, location.longitude)
}