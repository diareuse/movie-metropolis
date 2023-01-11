package movie.core.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.db.PerformanceTracer

@Module
@InstallIn(SingletonComponent::class)
class TracingModule {

    @Provides
    fun database(): PerformanceTracer = object : PerformanceTracer {
        override fun trace(tag: String): PerformanceTracer.Trace {
            val trace = Firebase.performance.newTrace(tag)
            trace.start()
            return PerformanceTracer.Trace(trace::stop)
        }
    }

}