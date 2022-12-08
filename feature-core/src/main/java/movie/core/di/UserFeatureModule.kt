package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.core.EventFeature
import movie.core.UserFeature
import movie.core.UserFeatureDatabase
import movie.core.UserFeatureImpl
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

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class UserFeatureModule {

    @Provides
    fun feature(
        service: UserService,
        event: EventFeature,
        bookingDao: BookingDao,
        seatsDao: BookingSeatsDao,
        movieDao: MovieDetailDao,
        cinemaDao: CinemaDao,
        mediaDao: MovieMediaDao,
        account: UserAccount
    ): UserFeature {
        var database: UserFeature
        database = UserFeatureDatabase(bookingDao, seatsDao, movieDao, cinemaDao, mediaDao)
        database = UserFeatureRecover(database)
        database = UserFeatureRequireNotEmpty(database)
        var network: UserFeature
        network = UserFeatureImpl(service, event, account)
        network = UserFeatureStoring(network, bookingDao, seatsDao)
        network = UserFeatureRecoverSecondary(database, network)
        return network
    }

}