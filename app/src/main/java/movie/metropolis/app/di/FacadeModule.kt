package movie.metropolis.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.calendar.CalendarList
import movie.core.EventCinemaFeature
import movie.core.EventDetailFeature
import movie.core.EventPreviewFeature
import movie.core.EventPromoFeature
import movie.core.EventShowingsFeature
import movie.core.FavoriteFeature
import movie.core.SetupFeature
import movie.core.TicketShareRegistry
import movie.core.UserBookingFeature
import movie.core.UserCredentialFeature
import movie.core.UserDataFeature
import movie.core.preference.EventPreference
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.booking.BookingFacadeFromFeature
import movie.metropolis.app.presentation.booking.BookingFacadeRecover
import movie.metropolis.app.presentation.cinema.CinemaFacade
import movie.metropolis.app.presentation.cinema.CinemaFacadeCaching
import movie.metropolis.app.presentation.cinema.CinemaFacadeFilterable
import movie.metropolis.app.presentation.cinema.CinemaFacadeFromFeature
import movie.metropolis.app.presentation.cinema.CinemaFacadeRecover
import movie.metropolis.app.presentation.cinema.CinemasFacade
import movie.metropolis.app.presentation.cinema.CinemasFacadeFromFeature
import movie.metropolis.app.presentation.cinema.CinemasFacadeRecover
import movie.metropolis.app.presentation.detail.MovieFacade
import movie.metropolis.app.presentation.detail.MovieFacadeFilterable
import movie.metropolis.app.presentation.detail.MovieFacadeFromFeature
import movie.metropolis.app.presentation.detail.MovieFacadeRecover
import movie.metropolis.app.presentation.home.HomeFacade
import movie.metropolis.app.presentation.home.HomeFacadeFromFeature
import movie.metropolis.app.presentation.listing.ListingAltFacade
import movie.metropolis.app.presentation.listing.ListingAltFacadeFromFeature
import movie.metropolis.app.presentation.listing.ListingAltFacadeRecover
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.listing.ListingFacadeFromFeature
import movie.metropolis.app.presentation.listing.ListingFacadeRecover
import movie.metropolis.app.presentation.login.LoginFacade
import movie.metropolis.app.presentation.login.LoginFacadeFromFeature
import movie.metropolis.app.presentation.order.OrderFacade
import movie.metropolis.app.presentation.order.OrderFacadeFromFeature
import movie.metropolis.app.presentation.order.OrderFacadeRecover
import movie.metropolis.app.presentation.profile.ProfileFacade
import movie.metropolis.app.presentation.profile.ProfileFacadeCaching
import movie.metropolis.app.presentation.profile.ProfileFacadeFromFeature
import movie.metropolis.app.presentation.profile.ProfileFacadeRecover
import movie.metropolis.app.presentation.settings.SettingsFacade
import movie.metropolis.app.presentation.settings.SettingsFacadeFromFeature
import movie.metropolis.app.presentation.settings.SettingsFacadeReactive
import movie.metropolis.app.presentation.settings.SettingsFacadeRecover
import movie.metropolis.app.presentation.setup.SetupFacade
import movie.metropolis.app.presentation.setup.SetupFacadeFromFeature
import movie.metropolis.app.presentation.share.ShareFacade
import movie.metropolis.app.presentation.share.ShareFacadeImageConvert
import movie.metropolis.app.presentation.share.ShareFacadeRecover
import movie.metropolis.app.presentation.share.ShareFacadeRefresh
import movie.metropolis.app.presentation.share.ShareFacadeText

@Module
@InstallIn(ActivityRetainedComponent::class)
class FacadeModule {

    @Provides
    fun booking(
        booking: UserBookingFeature,
        share: TicketShareRegistry
    ): BookingFacade {
        var facade: BookingFacade
        facade = BookingFacadeFromFeature(booking, share)
        facade = BookingFacadeRecover(facade)
        return facade
    }

    @Provides
    fun cinemas(event: EventCinemaFeature): CinemasFacade {
        var facade: CinemasFacade
        facade = CinemasFacadeFromFeature(event)
        facade = CinemasFacadeRecover(facade)
        return facade
    }

    @Provides
    fun cinema(
        cinemas: EventCinemaFeature,
        showings: EventShowingsFeature.Factory
    ) = CinemaFacade.Factory {
        var facade: CinemaFacade
        facade = CinemaFacadeFromFeature(it, cinemas, showings)
        facade = CinemaFacadeCaching(facade)
        facade = CinemaFacadeFilterable(facade)
        facade = CinemaFacadeRecover(facade)
        facade
    }

    @Provides
    fun movie(
        showings: EventShowingsFeature.Factory,
        detail: EventDetailFeature,
        favorite: FavoriteFeature
    ) = MovieFacade.Factory {
        var facade: MovieFacade
        facade = MovieFacadeFromFeature(it, showings, detail, favorite)
        facade = MovieFacadeFilterable(facade)
        facade = MovieFacadeRecover(facade)
        facade
    }

    @Provides
    fun listing(
        preview: EventPreviewFeature.Factory,
        favorite: FavoriteFeature
    ): ListingFacade {
        var facade: ListingFacade
        facade = ListingFacadeFromFeature(preview, favorite)
        facade = ListingFacadeRecover(facade)
        return facade
    }

    @Provides
    fun listingAlt(
        preview: EventPreviewFeature.Factory,
        favorite: FavoriteFeature,
        promo: EventPromoFeature
    ): ListingAltFacade.Factory {
        return object : ListingAltFacade.Factory {
            override fun upcoming(): ListingAltFacade = create(preview.upcoming())
            override fun current(): ListingAltFacade = create(preview.current())

            private fun create(preview: EventPreviewFeature): ListingAltFacade {
                var out: ListingAltFacade
                out = ListingAltFacadeFromFeature(preview, favorite, promo)
                out = ListingAltFacadeRecover(out)
                return out
            }
        }
    }

    @Provides
    fun profile(
        cinema: EventCinemaFeature,
        user: UserDataFeature,
        credential: UserCredentialFeature
    ): ProfileFacade {
        var facade: ProfileFacade
        facade = ProfileFacadeFromFeature(user, cinema, credential)
        facade = ProfileFacadeCaching(facade)
        facade = ProfileFacadeRecover(facade)
        return facade
    }

    @Provides
    fun login(
        user: UserCredentialFeature,
        setup: SetupFeature
    ): LoginFacade {
        val facade: LoginFacade
        facade = LoginFacadeFromFeature(user, setup)
        return facade
    }

    @Provides
    fun order(user: UserCredentialFeature): OrderFacade.Factory = OrderFacade.Factory {
        var facade: OrderFacade
        facade = OrderFacadeFromFeature(it, user)
        facade = OrderFacadeRecover(facade)
        facade
    }

    @Provides
    fun settings(
        prefs: EventPreference,
        calendars: CalendarList
    ): SettingsFacade {
        var facade: SettingsFacade
        facade = SettingsFacadeFromFeature(prefs, calendars)
        facade = SettingsFacadeReactive(facade)
        facade = SettingsFacadeRecover(facade)
        return facade
    }

    @Provides
    fun home(user: UserCredentialFeature): HomeFacade {
        return HomeFacadeFromFeature(user)
    }

    @Provides
    fun share(
        share: TicketShareRegistry,
        user: UserBookingFeature
    ): ShareFacade {
        var facade: ShareFacade
        facade = ShareFacadeText(share)
        facade = ShareFacadeImageConvert(facade)
        facade = ShareFacadeRecover(facade)
        facade = ShareFacadeRefresh(facade, user)
        return facade
    }

    @Provides
    fun setup(
        feature: SetupFeature
    ): SetupFacade {
        val facade: SetupFacade
        facade = SetupFacadeFromFeature(feature)
        return facade
    }

}