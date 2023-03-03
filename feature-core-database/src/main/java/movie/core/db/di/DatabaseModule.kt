package movie.core.db.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.core.db.MovieDatabase
import movie.core.db.PerformanceTracer
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
            .addMigrations(MovieDatabase.Migration2to3())
            .addMigrations(MovieDatabase.Migration7to8())
            .addMigrations(MovieDatabase.Migration10to11())
            .build()
    }

    @Provides
    internal fun booking(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): BookingDao {
        var dao: BookingDao = database.booking()
        dao = BookingDaoLowercase(dao)
        dao = BookingDaoPerformance(dao, tracer)
        dao = BookingDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun bookingSeats(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): BookingSeatsDao {
        var dao: BookingSeatsDao = database.bookingSeats()
        dao = BookingSeatsDaoLowercase(dao)
        dao = BookingSeatsDaoPerformance(dao, tracer)
        dao = BookingSeatsDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun cinema(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): CinemaDao {
        var dao: CinemaDao = database.cinema()
        dao = CinemaDaoLowercase(dao)
        dao = CinemaDaoPerformance(dao, tracer)
        dao = CinemaDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun movie(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): MovieDao {
        var dao: MovieDao = database.movie()
        dao = MovieDaoLowercase(dao)
        dao = MovieDaoPerformance(dao, tracer)
        dao = MovieDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun movieDetail(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): MovieDetailDao {
        var dao: MovieDetailDao = database.movieDetail()
        dao = MovieDetailDaoLowercase(dao)
        dao = MovieDetailDaoPerformance(dao, tracer)
        dao = MovieDetailDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun movieMedia(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): MovieMediaDao {
        var dao: MovieMediaDao = database.movieMedia()
        dao = MovieMediaDaoLowercase(dao)
        dao = MovieMediaDaoPerformance(dao, tracer)
        dao = MovieMediaDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun showing(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): ShowingDao {
        var dao: ShowingDao = database.showing()
        dao = ShowingDaoLowercase(dao)
        dao = ShowingDaoPerformance(dao, tracer)
        dao = ShowingDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun movieReference(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): MovieReferenceDao {
        var dao: MovieReferenceDao = database.movieReference()
        dao = MovieReferenceDaoLowercase(dao)
        dao = MovieReferenceDaoPerformance(dao, tracer)
        dao = MovieReferenceDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun moviePreview(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): MoviePreviewDao {
        var dao: MoviePreviewDao = database.moviePreview()
        dao = MoviePreviewDaoLowercase(dao)
        dao = MoviePreviewDaoPerformance(dao, tracer)
        dao = MoviePreviewDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun movieFavorite(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): MovieFavoriteDao {
        var dao: MovieFavoriteDao = database.movieFavorite()
        dao = MovieFavoriteDaoLowercase(dao)
        dao = MovieFavoriteDaoPerformance(dao, tracer)
        dao = MovieFavoriteDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun movieRating(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): MovieRatingDao {
        var dao: MovieRatingDao = database.movieRating()
        dao = MovieRatingDaoLowercase(dao)
        dao = MovieRatingDaoPerformance(dao, tracer)
        dao = MovieRatingDaoThreading(dao)
        return dao
    }

    @Provides
    internal fun moviePromo(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): MoviePromoDao {
        var dao: MoviePromoDao = database.moviePromo()
        dao = MoviePromoDaoLowercase(dao)
        dao = MoviePromoDaoPerformance(dao, tracer)
        dao = MoviePromoDaoThreading(dao)
        return dao
    }

}