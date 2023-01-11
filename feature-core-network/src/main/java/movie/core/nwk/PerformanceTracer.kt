package movie.core.nwk

interface PerformanceTracer {

    fun trace(tag: String): Trace

    interface Trace {
        fun setState(isSuccess: Boolean)
        fun stop()
    }

}

object PerformanceTracerNoop : PerformanceTracer {
    private val traceNoop = object : PerformanceTracer.Trace {
        override fun setState(isSuccess: Boolean) = Unit
        override fun stop() = Unit
    }

    override fun trace(tag: String) = traceNoop
}

inline fun <T> PerformanceTracer.trace(tag: String, body: (PerformanceTracer.Trace) -> T): T {
    val trace = trace(tag)
    return body(trace).also {
        trace.stop()
    }
}