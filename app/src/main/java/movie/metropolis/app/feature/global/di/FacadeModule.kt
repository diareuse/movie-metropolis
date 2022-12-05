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
import movie.metropolis.app.screen.cinema.CinemaFacade
import movie.metropolis.app.screen.cinema.CinemaFacadeCaching
import movie.metropolis.app.screen.cinema.CinemaFacadeFromFeature
import movie.metropolis.app.screen.cinema.CinemaFacadeRecover
import movie.metropolis.app.screen.cinema.CinemasFacade
import movie.metropolis.app.screen.cinema.CinemasFacadeFromFeature
import movie.metropolis.app.screen.cinema.CinemasFacadeRecover
import movie.metropolis.app.screen.detail.MovieFacade
import movie.metropolis.app.screen.detail.MovieFacadeFromFeature
import movie.metropolis.app.screen.detail.MovieFacadeRecover
import movie.metropolis.app.screen.listing.ListingFacade
import movie.metropolis.app.screen.listing.ListingFacadeFromFeature
import movie.metropolis.app.screen.listing.ListingFacadeRecover
import movie.metropolis.app.screen.profile.ProfileFacade
import movie.metropolis.app.screen.profile.ProfileFacadeCaching
import movie.metropolis.app.screen.profile.ProfileFacadeFromFeature
import movie.metropolis.app.screen.profile.ProfileFacadeRecover

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
        var facade: CinemasFacade
        facade = CinemasFacadeFromFeature(event)
        facade = CinemasFacadeRecover(facade)
        return facade
    }

    @Provides
    fun cinema(event: EventFeature) = CinemaFacade.Factory {
        var facade: CinemaFacade
        facade = CinemaFacadeFromFeature(it, event)
        facade = CinemaFacadeCaching(facade)
        facade = CinemaFacadeRecover(facade)
        facade
    }

    @Provides
    fun movie(event: EventFeature) = MovieFacade.Factory {
        var facade: MovieFacade
        facade = MovieFacadeFromFeature(it, event)
        facade = MovieFacadeRecover(facade)
        facade
    }

    @Provides
    fun listing(event: EventFeature): ListingFacade {
        var facade: ListingFacade
        facade = ListingFacadeFromFeature(event)
        facade = ListingFacadeRecover(facade)
        return facade
    }

    @Provides
    fun profile(
        event: EventFeature,
        user: UserFeature
    ): ProfileFacade {
        var facade: ProfileFacade
        facade = ProfileFacadeFromFeature(user, event)
        facade = ProfileFacadeCaching(facade)
        facade = ProfileFacadeRecover(facade)
        return facade
    }

}