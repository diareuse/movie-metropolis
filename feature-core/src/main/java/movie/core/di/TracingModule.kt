package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.nwk.PerformanceTracer
import movie.core.db.PerformanceTracer as DbTracer
import movie.core.nwk.PerformanceTracer as NetworkTracer

@Module
@InstallIn(SingletonComponent::class)
class TracingModule {

    @Provides
    @Reusable
    fun database(): DbTracer = object : DbTracer {
        override fun trace(tag: String): DbTracer.Trace {
            return DbTracer.Trace {}
        }
    }

    @Provides
    @Reusable
    fun network(): NetworkTracer = object : NetworkTracer {
        override fun trace(url: String, method: String): PerformanceTracer.Trace {
            return object : NetworkTracer.Trace {
                override fun stop() = Unit
                override fun setResponseCode(code: Int) = Unit
                override fun setRequestLength(length: Long) = Unit
                override fun setResponseLength(length: Long) = Unit
                override fun setAttribute(name: String, value: String) = Unit
            }
        }
    }

}