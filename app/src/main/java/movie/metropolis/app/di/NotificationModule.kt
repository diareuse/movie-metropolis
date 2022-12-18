package movie.metropolis.app.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.metropolis.app.feature.notification.NotificationInfoProviderCompound
import movie.core.notification.NotificationInfoProvider as FeatureCoreProvider

@Module
@InstallIn(SingletonComponent::class)
interface NotificationModule {

    @Binds
    fun featureCore(provider: NotificationInfoProviderCompound): FeatureCoreProvider

    companion object {

        @Provides
        fun provider(
            @ApplicationContext
            context: Context
        ): NotificationInfoProviderCompound = NotificationInfoProviderCompound(context)

    }

}