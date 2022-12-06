package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.core.EventFeature
import movie.core.EventFeatureImpl
import movie.core.nwk.CinemaService
import movie.core.nwk.EventService

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class EventFeatureModule {

    @Provides
    fun feature(
        event: EventService,
        cinema: CinemaService
    ): EventFeature {
        return EventFeatureImpl(event, cinema)
    }

}