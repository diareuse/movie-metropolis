package movie.pulse.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import movie.pulse.ExactPulse
import movie.pulse.ExactPulseScheduler

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ExactPulseEntryPoint {

    fun pulses(): Set<ExactPulse>
    fun scheduler(): ExactPulseScheduler

    companion object {

        operator fun invoke(context: Context) =
            EntryPointAccessors.fromApplication(context, ExactPulseEntryPoint::class.java)

    }

}