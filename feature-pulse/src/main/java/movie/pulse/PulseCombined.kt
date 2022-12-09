package movie.pulse

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PulseCombined(
    private vararg val pulses: Pulse
) : Pulse {

    override suspend fun execute(): Result<Any> {
        val results = coroutineScope {
            pulses.map { async { it.execute() } }.awaitAll()
        }
        val builder = MultiException.Builder()
        for (result in results)
            builder.add(result.exceptionOrNull() ?: continue)
        return builder.asResult()
    }

    class MultiException(
        private val causes: Iterable<Throwable>
    ) : RuntimeException() {

        override fun printStackTrace() {
            for (cause in causes)
                cause.printStackTrace()
        }

        class Builder {

            private val exceptions = mutableListOf<Throwable>()

            fun add(throwable: Throwable) = apply {
                exceptions += throwable
            }

            fun asResult() = when (exceptions.isEmpty()) {
                true -> Result.success(Unit)
                else -> Result.failure(MultiException(exceptions))
            }

        }
    }

}