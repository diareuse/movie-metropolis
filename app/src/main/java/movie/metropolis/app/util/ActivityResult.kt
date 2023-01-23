package movie.metropolis.app.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <I, O> ActivityResultRegistry.register(
    key: String,
    contract: ActivityResultContract<I, O>,
    input: I
): O = suspendCoroutine { cont ->
    register(key, contract) {
        cont.resume(it)
    }.launch(input)
}

fun Context.findActivity(): Activity {
    when (this) {
        is Activity -> return this
        is ContextWrapper -> return baseContext.findActivity()
    }
    throw IllegalStateException("Unknown Context $this")
}