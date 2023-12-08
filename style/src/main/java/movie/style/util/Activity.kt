package movie.style.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.findActivity(): Activity {
    return findActivityOrNull() ?: error("Unknown Context $this")
}

fun Context.findActivityOrNull(): Activity? {
    when (this) {
        is Activity -> return this
        is ContextWrapper -> return baseContext.findActivityOrNull()
    }
    return null
}