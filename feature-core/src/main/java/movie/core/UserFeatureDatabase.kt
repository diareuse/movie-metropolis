package movie.core

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import movie.core.adapter.BookingActiveFromDatabase
import movie.core.adapter.BookingExpiredFromDatabase
import movie.core.auth.UserAccount
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.model.Booking
import movie.core.model.FieldUpdate
import movie.core.model.SignInMethod
import movie.core.model.User

class UserFeatureDatabase(
    private val bookingDao: BookingDao,
    private val seatsDao: BookingSeatsDao,
    private val movieDao: MovieDetailDao,
    private val cinemaDao: CinemaDao,
    private val mediaDao: MovieMediaDao,
    private val account: UserAccount
) : UserFeature {

    override val email: String?
        get() = account.email

    override suspend fun signIn(method: SignInMethod): Result<Unit> {
        return Result.failure(NotImplementedError())
    }

    override suspend fun update(data: Iterable<FieldUpdate>): Result<User> {
        return Result.failure(NotImplementedError())
    }

    override suspend fun getUser(): Result<User> {
        return Result.failure(NotImplementedError())
    }

    override suspend fun getBookings(): Result<Iterable<Booking>> = coroutineScope {
        bookingDao.selectAll().map { booking ->
            val seats = seatsDao.select(booking.id)
            val movie = async { movieDao.select(booking.movieId) }
            val cinema = async { cinemaDao.select(booking.cinemaId) }
            val media = async { mediaDao.select(booking.movieId) }
            when (seats.isEmpty()) {
                true -> BookingExpiredFromDatabase(
                    bookingStored = booking,
                    movieStored = movie.await(),
                    cinemaStored = cinema.await(),
                    mediaStored = media.await()
                )

                else -> BookingActiveFromDatabase(
                    bookingStored = booking,
                    movieStored = movie.await(),
                    cinemaStored = cinema.await(),
                    mediaStored = media.await(),
                    seatsStored = seats
                )
            }
        }.let(Result.Companion::success)
    }

    override suspend fun getToken(): Result<String> {
        return Result.failure(NotImplementedError())
    }

}