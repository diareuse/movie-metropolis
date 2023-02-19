package movie.core

import java.util.Date
import kotlin.time.Duration

class ExpiredException(
    private val timestamp: Date,
    private val threshold: Duration
) : RuntimeException() {

    override val message: String
        get() = "[now=${System.currentTimeMillis()}, timestamp=${timestamp.time}, threshold=${threshold.inWholeMilliseconds}]"

}