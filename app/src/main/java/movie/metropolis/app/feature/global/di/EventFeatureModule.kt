package movie.metropolis.app.feature.global.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.metropolis.app.feature.global.CinemaService
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.EventFeatureImpl
import movie.metropolis.app.feature.global.EventService

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class EventFeatureModule {

    @Provides
    fun feature(event: EventService, cinema: CinemaService): EventFeature {
        return EventFeatureImpl(event, cinema)
    }

}