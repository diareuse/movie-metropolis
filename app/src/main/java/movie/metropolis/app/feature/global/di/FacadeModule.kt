package movie.metropolis.app.feature.global.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.UserFeature
import movie.metropolis.app.screen.booking.BookingFacade
import movie.metropolis.app.screen.booking.BookingFacadeFromFeature
import movie.metropolis.app.screen.booking.BookingFacadeRecover
import movie.metropolis.app.screen.cinema.CinemasFacade
import movie.metropolis.app.screen.cinema.CinemasFacadeFromFeature

@Module
@InstallIn(ActivityRetainedComponent::class)
class FacadeModule {

    @Provides
    fun booking(user: UserFeature): BookingFacade {
        var facade: BookingFacade
        facade = BookingFacadeFromFeature(user)
        facade = BookingFacadeRecover(facade)
        return facade
    }

    @Provides
    fun cinemas(event: EventFeature): CinemasFacade {
        return CinemasFacadeFromFeature(event)
    }

}