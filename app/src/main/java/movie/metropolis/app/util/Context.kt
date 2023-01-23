package movie.metropolis.app.util

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

val Context.isConnectionMetered
    get() = getSystemService<ConnectivityManager>()?.isActiveNetworkMetered ?: true