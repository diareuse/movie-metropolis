package movie.metropolis.app.feature.global.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import io.ktor.client.HttpClient
import movie.metropolis.app.feature.global.EventFeature

@Module
@InstallIn(ActivityRetainedComponent::class)
class EventFeatureModule {

    @Provides
    fun feature(client: HttpClient): EventFeature {
        TODO()
    }

}