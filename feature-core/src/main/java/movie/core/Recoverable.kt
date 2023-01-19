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

    companion object {

        inline fun <T, R> Array<T>.foldCatching(body: (T) -> R): R {
            val exception = Throwable("Folding resulted in error")
            for (element in this) try {
                return body(element)
            } catch (e: Throwable) {
                exception.addSuppressed(e)
                continue
            }
            throw exception
        }

    }

}