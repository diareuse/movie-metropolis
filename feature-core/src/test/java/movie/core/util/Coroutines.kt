package movie.core.util

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job

suspend fun awaitChildJobCompletion() {
    currentCoroutineContext().job.children.forEach { it.join() }
}