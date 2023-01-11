package movie.log

inline fun <T, R> T.flatMapCatching(
    body: T.() -> Result<R>
) = runCatching { body().getOrThrow() }

fun <T> Result<T>.logSevere(tag: String = "MM") = onFailure {
    Logger.error(tag, it.message ?: "Exception occurred", it)
}

fun <T> Result<T>.log(tag: String = "MM") = onFailure {
    Logger.verbose(tag, it.message ?: "Exception occurred", it)
}