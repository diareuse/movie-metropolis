package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import movie.calendar.CalendarWriter
import movie.core.EventCinemaFeature
import movie.core.EventDetailFeature
import movie.core.TicketStore
import movie.core.UserBookingFeature
import movie.core.UserBookingFeatureCalendar
import movie.core.UserBookingFeatureCatch
import movie.core.UserBookingFeatureDatabase
import movie.core.UserBookingFeatureDrainTickets
import movie.core.UserBookingFeatureFold
import movie.core.UserBookingFeatureInvalidateAfter
import movie.core.UserBookingFeatureLoginBypass
import movie.core.UserBookingFeatureNetwork
import movie.core.UserBookingFeatureRequireNotEmpty
import movie.core.UserBookingFeatureSaveTimestamp
import movie.core.UserBookingFeatureStoring
import movie.core.UserBookingFeatureWear
import movie.core.UserCredentialFeature
import movie.core.UserCredentialFeatureNetwork
import movie.core.UserDataFeature
import movie.core.UserDataFeatureChain
import movie.core.UserDataFeatureFold
import movie.core.UserDataFeatureNetwork
import movie.core.UserDataFeatureStored
import movie.core.UserDataFeatureStoring
import movie.core.auth.UserAccount
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.MovieDao
import movie.core.nwk.UserService
import movie.core.preference.EventPreference
import movie.core.preference.SyncPreference
import movie.core.preference.UserPreference
import movie.wear.WearService
import kotlin.time.Duration.Companion.hours

@Module
@InstallIn(SingletonComponent::class)
internal class UserFeatureModule {

    @Provides
    @Reusable
    fun credential(
        service: UserService,
        account: UserAccount
    ): UserCredentialFeature {
        return UserCredentialFeatureNetwork(service, account)
    }

    @Provides
    @Reusable
    fun data(
        service: UserService,
        cinema: EventCinemaFeature,
        preference: UserPreference
    ): UserDataFeature {
        var network: UserDataFeature
        network = UserDataFeatureNetwork(service, cinema)
        network = UserDataFeatureStoring(network, preference)
        var out: UserDataFeature
        out = UserDataFeatureStored(preference, cinema)
        out = UserDataFeatureChain(
            UserDataFeatureFold(out, network),
            UserDataFeatureFold(network, out)
        )
        return out
    }

    @Provides
    @Reusable
    fun booking(
        booking: BookingDao,
        seats: BookingSeatsDao,
        service: UserService,
        cinema: EventCinemaFeature,
        detail: EventDetailFeature,
        writer: CalendarWriter.Factory,
        preference: EventPreference,
        store: TicketStore,
        sync: SyncPreference,
        wear: WearService,
        scope: CoroutineScope,
        movie: MovieDao
    ): UserBookingFeature {
        var db: UserBookingFeature
        db = UserBookingFeatureDatabase(booking, seats, cinema)
        db = UserBookingFeatureRequireNotEmpty(db)
        var out: UserBookingFeature
        out = UserBookingFeatureNetwork(service, cinema)
        out = UserBookingFeatureCatch(out)
        out = UserBookingFeatureLoginBypass(out)
        out = UserBookingFeatureDrainTickets(out, detail, cinema, store)
        out = UserBookingFeatureStoring(out, booking, seats, scope)
        out = UserBookingFeatureSaveTimestamp(out, sync)
        out = UserBookingFeatureCalendar(out, writer, preference, scope, movie)
        out = UserBookingFeatureFold(UserBookingFeatureInvalidateAfter(db, sync, 1.hours), out, db)
        out = UserBookingFeatureWear(out, wear, scope)
        out = UserBookingFeatureCatch(out)
        return out
    }

}