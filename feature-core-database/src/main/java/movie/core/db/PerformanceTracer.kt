package movie.core.db

interface PerformanceTracer {

    fun trace(tag: String): Trace

    fun interface Trace {
        fun stop()
    }

}

inline fun <T> PerformanceTracer.trace(tag: String, body: () -> T): T {
    val trace = trace(tag)
    return body().also {
        trace.stop()
    }
}