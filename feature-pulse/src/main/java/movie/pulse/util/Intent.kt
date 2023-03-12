package movie.pulse.util

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import java.io.Serializable

inline fun <reified T : Serializable?> Intent.getSerializableExtraCompat(
    name: String
) = if (SDK_INT >= TIRAMISU) {
    getSerializableExtra(name, T::class.java)
} else {
    @Suppress("DEPRECATION")
    getSerializableExtra(name) as? T
}