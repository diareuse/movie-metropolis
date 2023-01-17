package movie.core.nwk

interface PerformanceTracer {

    fun trace(url: String, method: String): Trace

    interface Trace {
        fun setAttribute(name: String, value: String)
        fun setResponseCode(code: Int)
        fun setRequestLength(length: Long)
        fun setResponseLength(length: Long)
        fun stop()
    }

}

object PerformanceTracerNoop : PerformanceTracer {
    private val traceNoop = object : PerformanceTracer.Trace {
        override fun setAttribute(name: String, value: String) = Unit
        override fun setResponseCode(code: Int) = Unit
        override fun setRequestLength(length: Long) = Unit
        override fun setResponseLength(length: Long) = Unit
        override fun stop() = Unit
    }

    override fun trace(url: String, method: String) = traceNoop
}

inline fun <T> PerformanceTracer.trace(
    url: String,
    method: String,
    body: (PerformanceTracer.Trace) -> T
): T {
    val trace = trace(url, method)
    return body(trace).also {
        trace.stop()
    }
}