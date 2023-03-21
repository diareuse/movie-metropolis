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
import movie.image.ImageAnalyzer
import movie.metropolis.app.feature.billing.BillingFacade
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.booking.BookingFacadeFromFeature
import movie.metropolis.app.presentation.booking.BookingFacadeRecover
import movie.metropolis.app.presentation.booking.BookingFacadeWithDetail
import movie.metropolis.app.presentation.booking.BookingFacadeWithSpotColor
import movie.metropolis.app.presentation.cinema.CinemaFacade
import movie.metropolis.app.presentation.cinema.CinemaFacadeFilterable
import movie.metropolis.app.presentation.cinema.CinemaFacadeFromFeature
import movie.metropolis.app.presentation.cinema.CinemaFacadeRecover
import movie.metropolis.app.presentation.cinema.CinemasFacade
import movie.metropolis.app.presentation.cinema.CinemasFacadeFromFeature
import movie.metropolis.app.presentation.cinema.CinemasFacadeRecover
import movie.metropolis.app.presentation.detail.MovieFacade
import movie.metropolis.app.presentation.detail.MovieFacadeFilterable
import movie.metropolis.app.presentation.detail.MovieFacadeFromFeature
import movie.metropolis.app.presentation.detail.MovieFacadeReactive
import movie.metropolis.app.presentation.detail.MovieFacadeRecover
import movie.metropolis.app.presentation.home.HomeFacade
import movie.metropolis.app.presentation.home.HomeFacadeFromFeature
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.listing.ListingFacadeCurrent
import movie.metropolis.app.presentation.listing.ListingFacadeRecover
import movie.metropolis.app.presentation.listing.ListingFacadeUpcoming
import movie.metropolis.app.presentation.login.LoginFacade
import movie.metropolis.app.presentation.login.LoginFacadeFromFeature
import movie.metropolis.app.presentation.order.OrderCompleteFacade
import movie.metropolis.app.presentation.order.OrderCompleteFacadeFromFeature
import movie.metropolis.app.presentation.order.OrderFacade
import movie.metropolis.app.presentation.order.OrderFacadeFromFeature
import movie.metropolis.app.presentation.order.OrderFacadeInvalidateBooking
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
import movie.rating.RatingProvider

@Module
@InstallIn(ActivityRetainedComponent::class)
class FacadeModule {

    @Provides
    fun booking(
        booking: UserBookingFeature,
        share: TicketShareRegistry,
        detail: EventDetailFeature,
        analyzer: ImageAnalyzer
    ): BookingFacade {
        var facade: BookingFacade
        facade = BookingFacadeFromFeature(booking, share)
        facade = BookingFacadeWithDetail(facade, detail)
        facade = BookingFacadeWithSpotColor(facade, analyzer)
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
        facade = CinemaFacadeRecover(facade)
        facade = CinemaFacadeFilterable(facade)
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
        facade = MovieFacadeReactive(facade)
        facade = MovieFacadeRecover(facade)
        facade = MovieFacadeFilterable(facade)
        facade
    }

    @Provides
    fun listing(
        preview: EventPreviewFeature.Factory,
        favorite: FavoriteFeature,
        promo: EventPromoFeature,
        analyzer: ImageAnalyzer,
        rating: RatingProvider.Composed,
        detail: EventDetailFeature
    ): ListingFacade.Factory {
        return object : ListingFacade.Factory {
            override fun upcoming(): ListingFacade =
                create(ListingFacadeUpcoming(preview.upcoming(), favorite, promo, analyzer))

            override fun current(): ListingFacade =
                create(ListingFacadeCurrent(preview.current(), promo, analyzer, rating, detail))

            private fun create(facade: ListingFacade): ListingFacade {
                var out: ListingFacade = facade
                out = ListingFacadeRecover(out)
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
    fun order(
        user: UserCredentialFeature,
        booking: BookingFacade
    ): OrderFacade.Factory = OrderFacade.Factory {
        var facade: OrderFacade
        facade = OrderFacadeFromFeature(it, user)
        facade = OrderFacadeInvalidateBooking(facade, booking)
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

    @Provides
    fun orderComplete(
        facade: BillingFacade
    ): OrderCompleteFacade {
        return OrderCompleteFacadeFromFeature(facade)
    }

}