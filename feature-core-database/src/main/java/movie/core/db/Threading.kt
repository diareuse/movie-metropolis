package movie.core.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend inline fun <T, R> T.io(crossinline body: suspend T.() -> R) =
    withContext(Dispatchers.IO) {
        body()
    }