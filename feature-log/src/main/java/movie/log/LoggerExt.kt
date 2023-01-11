package movie.log

inline fun <T, R> T.logCatching(
    tag: String,
    body: T.() -> R
) = runCatching(body).onFailure {
    Logger.error(tag, it.message ?: "Exception occurred", it)
}

inline fun <T, R> T.logCatchingResult(
    tag: String,
    body: T.() -> Result<R>
) = runCatching { body().getOrThrow() }.onFailure {
    Logger.error(tag, it.message ?: "Exception occurred", it)
}