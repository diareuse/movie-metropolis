package movie.pulse.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import movie.pulse.ExactPulse

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ExactPulseEntryPoint {

    fun pulses(): Set<ExactPulse>

    companion object {

        operator fun invoke(context: Context) =
            EntryPointAccessors.fromApplication(context, ExactPulseEntryPoint::class.java)

    }

}