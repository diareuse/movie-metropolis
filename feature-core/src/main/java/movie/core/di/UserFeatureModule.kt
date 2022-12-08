package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.core.EventFeature
import movie.core.UserFeature
import movie.core.UserFeatureDatabase
import movie.core.UserFeatureImpl
import movie.core.UserFeatureStoring
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.model.FieldUpdate
import movie.core.model.SignInMethod
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
    ): UserFeature {
        var database: UserFeature
        database = UserFeatureDatabase(bookingDao, seatsDao, movieDao, cinemaDao, mediaDao)
        database = UserFeatureRecover(database)
        var network: UserFeature
        network = UserFeatureImpl(service, event)
        network = UserFeatureStoring(network, bookingDao, seatsDao)
        network = UserFeatureRecoverSecondary(database, network)
        return network
    }

}

class UserFeatureRecover(
    private val origin: UserFeature
) : UserFeature {

    override suspend fun signIn(method: SignInMethod) =
        origin.runCatching { signIn(method).getOrThrow() }

    override suspend fun update(data: Iterable<FieldUpdate>) =
        origin.runCatching { update(data).getOrThrow() }

    override suspend fun getUser() =
        origin.runCatching { getUser().getOrThrow() }

    override suspend fun getBookings() =
        origin.runCatching { getBookings().getOrThrow() }

    override suspend fun getToken() =
        origin.runCatching { getToken().getOrThrow() }

}

class UserFeatureRecoverSecondary(
    private val primary: UserFeature,
    private val secondary: UserFeature
) : UserFeature {

    override suspend fun signIn(method: SignInMethod) = tryOrRecover {
        signIn(method)
    }

    override suspend fun update(data: Iterable<FieldUpdate>) = tryOrRecover {
        update(data)
    }

    override suspend fun getUser() = tryOrRecover {
        getUser()
    }

    override suspend fun getBookings() = tryOrRecover {
        getBookings()
    }

    override suspend fun getToken() = tryOrRecover {
        getToken()
    }

    // ---

    private inline fun <T> tryOrRecover(body: UserFeature.() -> Result<T>): Result<T> {
        return primary.run(body).recoverCatching { secondary.run(body).getOrThrow() }
    }

}