package movie.metropolis.app.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.calendar.CalendarList
import movie.cinema.city.CinemaCity
import movie.cinema.city.RegionProvider
import movie.core.auth.UserAccount
import movie.metropolis.app.feature.billing.BillingFacade
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.booking.BookingFacadeCinemaCity
import movie.metropolis.app.presentation.cinema.CinemaFacade
import movie.metropolis.app.presentation.cinema.CinemaFacadeCinemaCity
import movie.metropolis.app.presentation.cinema.CinemasFacade
import movie.metropolis.app.presentation.cinema.CinemasFacadeCinemaCity
import movie.metropolis.app.presentation.detail.MovieFacade
import movie.metropolis.app.presentation.detail.MovieFacadeCinemaCity
import movie.metropolis.app.presentation.detail.MovieFacadeRating
import movie.metropolis.app.presentation.detail.MovieFacadeWithActors
import movie.metropolis.app.presentation.home.HomeFacade
import movie.metropolis.app.presentation.home.HomeFacadeFromFeature
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.listing.ListingFacadeCinemaCity
import movie.metropolis.app.presentation.listing.ListingFacadeWithRating
import movie.metropolis.app.presentation.login.LoginFacade
import movie.metropolis.app.presentation.login.LoginFacadeFromFeature
import movie.metropolis.app.presentation.order.OrderCompleteFacade
import movie.metropolis.app.presentation.order.OrderCompleteFacadeFromFeature
import movie.metropolis.app.presentation.order.OrderFacade
import movie.metropolis.app.presentation.order.OrderFacadeFromFeature
import movie.metropolis.app.presentation.profile.ProfileFacade
import movie.metropolis.app.presentation.profile.ProfileFacadeCinemaCity
import movie.metropolis.app.presentation.settings.SettingsFacade
import movie.metropolis.app.presentation.settings.SettingsFacadeFromFeature
import movie.metropolis.app.presentation.setup.SetupFacade
import movie.metropolis.app.presentation.setup.SetupFacadeFromFeature
import movie.metropolis.app.presentation.ticket.TicketFacade
import movie.metropolis.app.presentation.ticket.TicketFacadeCinemaCinemaCity
import movie.metropolis.app.presentation.ticket.TicketFacadeMovieCinemaCity
import movie.rating.ActorProvider
import movie.rating.MetadataProvider
import movie.settings.GlobalPreferences

@Module
@InstallIn(ActivityRetainedComponent::class)
class FacadeModule {

    @Provides
    @Reusable
    fun booking(
        cinemaCity: CinemaCity
    ): BookingFacade {
        return BookingFacadeCinemaCity(cinemaCity)
    }

    @Provides
    @Reusable
    fun cinemas(cinemaCity: CinemaCity): CinemasFacade {
        return CinemasFacadeCinemaCity(cinemaCity)
    }

    @Provides
    @Reusable
    fun cinema(
        cinemaCity: CinemaCity
    ): CinemaFacade.Factory = CinemaFacade.Factory {
        CinemaFacadeCinemaCity(it, cinemaCity)
    }

    @Provides
    @Reusable
    fun movie(
        cinemaCity: CinemaCity,
        rating: MetadataProvider,
        actors: ActorProvider
    ): MovieFacade.Factory = MovieFacade.Factory {
        var facade: MovieFacade
        facade = MovieFacadeCinemaCity(it, cinemaCity)
        facade = MovieFacadeRating(facade, rating)
        facade = MovieFacadeWithActors(facade, actors)
        facade
    }

    @Provides
    @Reusable
    fun listing(
        cinemaCity: CinemaCity,
        rating: MetadataProvider
    ): ListingFacade.Factory {
        return object : ListingFacade.Factory {
            override fun upcoming(): ListingFacade = create(true)
            override fun current(): ListingFacade = create(false)

            private fun create(future: Boolean): ListingFacade {
                var out: ListingFacade
                out = ListingFacadeCinemaCity(future, cinemaCity)
                out = ListingFacadeWithRating(out, rating)
                return out
            }
        }
    }

    @Provides
    @Reusable
    fun profile(
        cinema: CinemaCity,
        user: UserAccount
    ): ProfileFacade {
        var facade: ProfileFacade
        facade = ProfileFacadeCinemaCity(cinema, user)
        return facade
    }

    @Provides
    @Reusable
    fun login(
        user: UserAccount,
        cinemaCity: CinemaCity,
        region: RegionProvider
    ): LoginFacade {
        val facade: LoginFacade
        facade = LoginFacadeFromFeature(user, cinemaCity, region)
        return facade
    }

    @Provides
    @Reusable
    fun order(
        cinemaCity: CinemaCity
    ): OrderFacade.Factory = OrderFacade.Factory {
        var facade: OrderFacade
        facade = OrderFacadeFromFeature(it, cinemaCity)
        facade
    }

    @Provides
    @Reusable
    fun settings(
        prefs: GlobalPreferences,
        calendars: CalendarList
    ): SettingsFacade {
        var facade: SettingsFacade
        facade = SettingsFacadeFromFeature(prefs, calendars)
        return facade
    }

    @Provides
    @Reusable
    fun home(user: UserAccount): HomeFacade {
        return HomeFacadeFromFeature(user)
    }

    @Provides
    @Reusable
    fun setup(
        feature: RegionProvider
    ): SetupFacade {
        val facade: SetupFacade
        facade = SetupFacadeFromFeature(feature)
        return facade
    }

    @Provides
    @Reusable
    fun orderComplete(
        facade: BillingFacade
    ): OrderCompleteFacade {
        return OrderCompleteFacadeFromFeature(facade)
    }

    @Provides
    @Reusable
    fun ticket(
        cinema: CinemaCity
    ) = object : TicketFacade.Factory {
        override fun movie(id: String): TicketFacade.LocationFactory {
            return TicketFacade.LocationFactory { _ ->
                TicketFacadeMovieCinemaCity(id, cinema)
            }
        }

        override fun cinema(id: String): TicketFacade.LocationFactory {
            return TicketFacade.LocationFactory { _ ->
                TicketFacadeCinemaCinemaCity(id, cinema)
            }
        }
    }

}