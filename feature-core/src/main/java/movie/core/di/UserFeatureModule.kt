package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.calendar.CalendarWriter
import movie.core.EventFeature
import movie.core.TicketStore
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
        event: EventFeature,
        bookingDao: BookingDao,
        seatsDao: BookingSeatsDao,
        account: UserAccount,
        writer: CalendarWriter.Factory,
        preference: EventPreference,
        store: TicketStore
    ): UserFeature {
        var network: UserFeature
        network = UserFeatureImpl(service, event, account)
        network = UserFeatureLoginBypass(network)
        network = UserFeatureDrainTickets(network, event, store)
        network = UserFeatureStoring(network, bookingDao, seatsDao)
        network = UserFeatureCalendar(network, writer, preference)
        return network
    }

}