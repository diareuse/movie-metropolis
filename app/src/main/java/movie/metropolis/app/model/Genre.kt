package movie.metropolis.app.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.*

@Immutable
@JvmInline
value class Genre(private val value: String) {

    @Stable
    @SuppressLint("DiscouragedApi")
    fun getName(context: Context): String {
        val name = value.lowercase().replace("-", "_")
        val identifier = context.resources.getIdentifier(name, "string", context.packageName)
        if (identifier == 0) return value.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        return context.getString(identifier)
    }

}