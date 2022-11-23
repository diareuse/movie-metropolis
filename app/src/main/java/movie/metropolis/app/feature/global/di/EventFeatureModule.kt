package movie.metropolis.app.feature.global.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import io.ktor.client.HttpClient
import movie.metropolis.app.di.ClientData
import movie.metropolis.app.di.ClientRoot
import movie.metropolis.app.feature.global.CinemaService
import movie.metropolis.app.feature.global.CinemaServiceImpl
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.EventFeatureImpl
import movie.metropolis.app.feature.global.EventService
import movie.metropolis.app.feature.global.EventServiceImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class EventFeatureModule {

    @Provides
    fun feature(event: EventService, cinema: CinemaService): EventFeature {
        return EventFeatureImpl(event, cinema)
    }

    @Provides
    fun event(
        @ClientData
        client: HttpClient
    ): EventService {
        return EventServiceImpl(client)
    }

    @Provides
    fun cinema(
        @ClientRoot
        client: HttpClient
    ): CinemaService {
        return CinemaServiceImpl(client)
    }

}