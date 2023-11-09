package movie.core.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import movie.core.FavoriteFeature
import movie.pulse.ExactPulseScheduler

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BootCompletedWorkerEntryPoint {

    fun favorite(): FavoriteFeature
    fun scheduler(): ExactPulseScheduler

    companion object {

        operator fun invoke(context: Context) =
            EntryPointAccessors.fromApplication(context, BootCompletedWorkerEntryPoint::class.java)

    }

}