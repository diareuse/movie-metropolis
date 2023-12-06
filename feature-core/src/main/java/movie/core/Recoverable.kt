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
            var exception: Throwable? = null
            for (element in this) try {
                return body(element)
            } catch (e: Throwable) {
                if (exception != null)
                    e.addSuppressed(exception)
                exception = e
                continue
            }
            throw exception ?: IndexOutOfBoundsException("Nothing to fold")
        }

    }

}

inline fun <T> wrap(body: () -> Result<T>): Result<T> {
    return runCatching { body().getOrThrow() }
}

inline fun <T, R> Array<T>.fold(body: T.() -> Result<R>): Result<R> {
    val error = IllegalStateException("All options were exhausted")
    for (option in this) {
        val result = body(option)
        if (result.isSuccess)
            return result
        result.onFailure {
            error.addSuppressed(it)
        }
    }
    return Result.failure(error)
}

inline fun <T, R> Array<T>.foldSimple(body: T.() -> R): R {
    val error = IllegalStateException("All options were exhausted")
    for (option in this) {
        try {
            return body(option)
        } catch (e: Throwable) {
            error.addSuppressed(e)
        }
    }
    throw error
}