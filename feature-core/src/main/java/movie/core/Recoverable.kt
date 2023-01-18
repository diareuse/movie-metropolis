package movie.core

interface Recoverable {

    fun <T> Result<T>.onFailureThrow() = onFailure { throw it }

    suspend fun <T> runCatchingResult(
        callback: ResultCallback<T>,
        body: suspend (callback: ResultCallback<T>) -> Unit
    ) {
        val error = runCatching { body { callback(it.onFailureThrow()) } }.onSuccess { return }
        @Suppress("UNCHECKED_CAST")
        callback(error as Result<T>)
    }

}