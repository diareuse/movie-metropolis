package movie.core.db.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.core.db.MovieDatabase
import movie.core.db.dao.*

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    internal fun database(
        @ApplicationContext
        context: Context
    ): MovieDatabase {
        val name = "${context.packageName}.core"
        return Room
            .databaseBuilder(context, MovieDatabase::class.java, name)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    internal fun booking(
        database: MovieDatabase
    ): BookingDao {
        var dao: BookingDao = database.booking()
        dao = BookingDaoLowercase(dao)
        return dao
    }

    @Provides
    internal fun bookingSeats(
        database: MovieDatabase
    ): BookingSeatsDao {
        var dao: BookingSeatsDao = database.bookingSeats()
        dao = BookingSeatsDaoLowercase(dao)
        return dao
    }

    @Provides
    internal fun cinema(
        database: MovieDatabase
    ): CinemaDao {
        var dao: CinemaDao = database.cinema()
        dao = CinemaDaoLowercase(dao)
        return dao
    }

    @Provides
    internal fun movie(
        database: MovieDatabase
    ): MovieDao {
        var dao: MovieDao = database.movie()
        dao = MovieDaoLowercase(dao)
        return dao
    }

    @Provides
    internal fun movieDetail(
        database: MovieDatabase
    ): MovieDetailDao {
        var dao: MovieDetailDao = database.movieDetail()
        dao = MovieDetailDaoLowercase(dao)
        return dao
    }

    @Provides
    internal fun movieMedia(
        database: MovieDatabase
    ): MovieMediaDao {
        var dao: MovieMediaDao = database.movieMedia()
        dao = MovieMediaDaoLowercase(dao)
        return dao
    }

    @Provides
    internal fun showing(
        database: MovieDatabase
    ): ShowingDao {
        var dao: ShowingDao = database.showing()
        dao = ShowingDaoLowercase(dao)
        return dao
    }

    @Provides
    internal fun movieReference(
        database: MovieDatabase
    ): MovieReferenceDao {
        var dao: MovieReferenceDao = database.movieReference()
        dao = MovieReferenceDaoLowercase(dao)
        return dao
    }

    @Provides
    internal fun moviePreview(
        database: MovieDatabase
    ): MoviePreviewDao {
        var dao: MoviePreviewDao = database.moviePreview()
        dao = MoviePreviewDaoLowercase(dao)
        return dao
    }

    @Provides
    internal fun movieFavorite(
        database: MovieDatabase
    ): MovieFavoriteDao {
        var dao: MovieFavoriteDao = database.movieFavorite()
        dao = MovieFavoriteDaoLowercase(dao)
        return dao
    }

}