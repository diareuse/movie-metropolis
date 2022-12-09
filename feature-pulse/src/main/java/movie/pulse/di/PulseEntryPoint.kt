package movie.pulse.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PulseEntryPoint {

    fun factory(): HiltWorkerFactory

    companion object {

        operator fun invoke(context: Context) = EntryPointAccessors
            .fromApplication(context, PulseEntryPoint::class.java)

    }

}