package movie.core.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
        override fun trace(tag: String): NetworkTracer.Trace {
            val trace = Firebase.performance.newTrace(tag)
            return object : NetworkTracer.Trace {
                override fun stop() = trace.stop()
                override fun setState(isSuccess: Boolean) =
                    trace.putAttribute("success", isSuccess.toString())
            }
        }
    }

}