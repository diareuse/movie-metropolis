package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.calendar.CalendarWriter
import movie.core.EventCinemaFeature
import movie.core.EventDetailFeature
import movie.core.TicketStore
import movie.core.UserCredentialFeature
import movie.core.UserCredentialFeatureNetwork
import movie.core.UserDataFeature
import movie.core.UserDataFeatureCatch
import movie.core.UserDataFeatureChain
import movie.core.UserDataFeatureFold
import movie.core.UserDataFeatureNetwork
import movie.core.UserDataFeatureStored
import movie.core.UserDataFeatureStoring
import movie.core.UserFeature
import movie.core.UserFeatureCalendar
import movie.core.UserFeatureDatabase
import movie.core.UserFeatureDrainTickets
import movie.core.UserFeatureImpl
import movie.core.UserFeatureLoginBypass
import movie.core.UserFeatureRecover
import movie.core.UserFeatureRecoverSecondary
import movie.core.UserFeatureRequireNotEmpty
import movie.core.UserFeatureStoring
import movie.core.auth.UserAccount
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.nwk.UserService
import movie.core.preference.EventPreference
import movie.core.preference.UserPreference

@Module
@InstallIn(SingletonComponent::class)
internal class UserFeatureModule {

    @Provides
    fun feature(
        account: UserAccount,
        bookingDao: BookingDao,
        seatsDao: BookingSeatsDao,
        movieDao: MovieDetailDao,
        cinemaDao: CinemaDao,
        mediaDao: MovieMediaDao,
        @Saving saving: UserFeature
    ): UserFeature {
        var database: UserFeature
        database = UserFeatureDatabase(bookingDao, seatsDao, movieDao, cinemaDao, mediaDao, account)
        database = UserFeatureRecover(database)
        database = UserFeatureRequireNotEmpty(database)
        val network: UserFeature
        network = UserFeatureRecoverSecondary(database, saving)
        return network
    }

    @Saving
    @Provides
    fun saving(
        service: UserService,
        cinema: EventCinemaFeature,
        movie: EventDetailFeature,
        bookingDao: BookingDao,
        seatsDao: BookingSeatsDao,
        account: UserAccount,
        writer: CalendarWriter.Factory,
        preference: EventPreference,
        store: TicketStore
    ): UserFeature {
        var network: UserFeature
        network = UserFeatureImpl(service, cinema, movie, account)
        network = UserFeatureLoginBypass(network)
        network = UserFeatureDrainTickets(network, movie, cinema, store)
        network = UserFeatureStoring(network, bookingDao, seatsDao)
        network = UserFeatureCalendar(network, writer, preference)
        return network
    }

    @Provides
    fun credential(
        service: UserService,
        account: UserAccount
    ): UserCredentialFeature {
        return UserCredentialFeatureNetwork(service, account)
    }

    @Provides
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
        out = UserDataFeatureCatch(out)
        return out
    }

}