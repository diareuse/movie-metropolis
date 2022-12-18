package movie.pulse.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.pulse.ExactPulseScheduler
import movie.pulse.ExactPulseSchedulerPlatform

@Module
@InstallIn(SingletonComponent::class)
class PulseModule {

    @Provides
    fun scheduler(
        @ApplicationContext
        context: Context
    ): ExactPulseScheduler {
        return ExactPulseSchedulerPlatform(context)
    }

}