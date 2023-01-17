package movie.core.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.nwk.PerformanceTracer
import movie.core.db.PerformanceTracer as DbTracer
import movie.core.nwk.PerformanceTracer as NetworkTracer

@Module
@InstallIn(SingletonComponent::class)
class TracingModule {

    @Provides
    fun database(): DbTracer = object : DbTracer {
        override fun trace(tag: String): DbTracer.Trace {
            val trace = Firebase.performance.newTrace(tag)
            trace.start()
            return DbTracer.Trace(trace::stop)
        }
    }

    @Provides
    fun network(): NetworkTracer = object : NetworkTracer {
        override fun trace(url: String, method: String): PerformanceTracer.Trace {
            val trace = Firebase.performance.newHttpMetric(url, method)
            trace.start()
            return object : NetworkTracer.Trace {
                override fun stop() = trace.stop()
                override fun setResponseCode(code: Int) = trace.setHttpResponseCode(code)
                override fun setRequestLength(length: Long) = trace.setRequestPayloadSize(length)
                override fun setResponseLength(length: Long) = trace.setResponsePayloadSize(length)
                override fun setAttribute(name: String, value: String) =
                    trace.putAttribute(name, value)
            }
        }
    }

}