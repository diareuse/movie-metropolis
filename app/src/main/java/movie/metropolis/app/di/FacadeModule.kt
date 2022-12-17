package movie.metropolis.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.core.EventFeature
import movie.core.FavoriteFeature
import movie.core.UserFeature
import movie.core.di.Saving
import movie.core.preference.EventPreference
import movie.metropolis.app.screen.booking.BookingFacade
import movie.metropolis.app.screen.booking.BookingFacadeFromFeature
import movie.metropolis.app.screen.booking.BookingFacadeRecover
import movie.metropolis.app.screen.cinema.CinemaFacade
import movie.metropolis.app.screen.cinema.CinemaFacadeCaching
import movie.metropolis.app.screen.cinema.CinemaFacadeFilterable
import movie.metropolis.app.screen.cinema.CinemaFacadeFromFeature
import movie.metropolis.app.screen.cinema.CinemaFacadeRecover
import movie.metropolis.app.screen.cinema.CinemasFacade
import movie.metropolis.app.screen.cinema.CinemasFacadeFromFeature
import movie.metropolis.app.screen.cinema.CinemasFacadeRecover
import movie.metropolis.app.screen.detail.MovieFacade
import movie.metropolis.app.screen.detail.MovieFacadeFilterable
import movie.metropolis.app.screen.detail.MovieFacadeFromFeature
import movie.metropolis.app.screen.detail.MovieFacadeRecover
import movie.metropolis.app.screen.listing.ListingFacade
import movie.metropolis.app.screen.listing.ListingFacadeFromFeature
import movie.metropolis.app.screen.listing.ListingFacadeRecover
import movie.metropolis.app.screen.order.OrderFacade
import movie.metropolis.app.screen.order.OrderFacadeFromFeature
import movie.metropolis.app.screen.order.OrderFacadeRecover
import movie.metropolis.app.screen.profile.LoginFacade
import movie.metropolis.app.screen.profile.LoginFacadeFromFeature
import movie.metropolis.app.screen.profile.ProfileFacade
import movie.metropolis.app.screen.profile.ProfileFacadeCaching
import movie.metropolis.app.screen.profile.ProfileFacadeFromFeature
import movie.metropolis.app.screen.profile.ProfileFacadeRecover
import movie.metropolis.app.screen.settings.SettingsFacade
import movie.metropolis.app.screen.settings.SettingsFacadeFromFeature
import movie.metropolis.app.screen.settings.SettingsFacadeReactive
import movie.metropolis.app.screen.settings.SettingsFacadeRecover

@Module
@InstallIn(ActivityRetainedComponent::class)
class FacadeModule {

    @Provides
    fun booking(
        user: UserFeature,
        @Saving online: UserFeature
    ): BookingFacade {
        var facade: BookingFacade
        facade = BookingFacadeFromFeature(user, online)
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
        facade = CinemaFacadeFilterable(facade)
        facade = CinemaFacadeRecover(facade)
        facade
    }

    @Provides
    fun movie(
        event: EventFeature,
        favorite: FavoriteFeature
    ) = MovieFacade.Factory {
        var facade: MovieFacade
        facade = MovieFacadeFromFeature(it, event, favorite)
        facade = MovieFacadeFilterable(facade)
        facade = MovieFacadeRecover(facade)
        facade
    }

    @Provides
    fun listing(
        event: EventFeature,
        favorite: FavoriteFeature
    ): ListingFacade {
        var facade: ListingFacade
        facade = ListingFacadeFromFeature(event, favorite)
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

    @Provides
    fun login(user: UserFeature): LoginFacade {
        val facade: LoginFacade
        facade = LoginFacadeFromFeature(user)
        return facade
    }

    @Provides
    fun order(user: UserFeature): OrderFacade.Factory = OrderFacade.Factory {
        var facade: OrderFacade
        facade = OrderFacadeFromFeature(it, user)
        facade = OrderFacadeRecover(facade)
        facade
    }

    @Provides
    fun settings(prefs: EventPreference): SettingsFacade {
        var facade: SettingsFacade
        facade = SettingsFacadeFromFeature(prefs)
        facade = SettingsFacadeReactive(facade)
        facade = SettingsFacadeRecover(facade)
        return facade
    }

}