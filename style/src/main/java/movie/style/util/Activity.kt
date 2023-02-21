package movie.style.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.findActivity(): Activity {
    when (this) {
        is Activity -> return this
        is ContextWrapper -> return baseContext.findActivity()
    }
    throw IllegalStateException("Unknown Context $this")
}