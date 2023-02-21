package movie.metropolis.app.util

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