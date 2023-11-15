package movie.core.db.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.core.db.MovieDatabase
import movie.core.db.PerformanceTracer
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingDaoLowercase
import movie.core.db.dao.BookingDaoPerformance
import movie.core.db.dao.BookingDaoThreading
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.BookingSeatsDaoLowercase
import movie.core.db.dao.BookingSeatsDaoPerformance
import movie.core.db.dao.BookingSeatsDaoThreading
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.CinemaDaoLowercase
import movie.core.db.dao.CinemaDaoPerformance
import movie.core.db.dao.CinemaDaoThreading
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDaoLowercase
import movie.core.db.dao.MovieDaoPerformance
import movie.core.db.dao.MovieDaoThreading
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieDetailDaoLowercase
import movie.core.db.dao.MovieDetailDaoPerformance
import movie.core.db.dao.MovieDetailDaoThreading
import movie.core.db.dao.MovieFavoriteDao
import movie.core.db.dao.MovieFavoriteDaoLowercase
import movie.core.db.dao.MovieFavoriteDaoPerformance
import movie.core.db.dao.MovieFavoriteDaoThreading
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MovieMediaDaoLowercase
import movie.core.db.dao.MovieMediaDaoPerformance
import movie.core.db.dao.MovieMediaDaoThreading
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MoviePreviewDaoLowercase
import movie.core.db.dao.MoviePreviewDaoPerformance
import movie.core.db.dao.MoviePreviewDaoThreading
import movie.core.db.dao.MoviePromoDao
import movie.core.db.dao.MoviePromoDaoLowercase
import movie.core.db.dao.MoviePromoDaoPerformance
import movie.core.db.dao.MoviePromoDaoThreading
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.MovieReferenceDaoLowercase
import movie.core.db.dao.MovieReferenceDaoPerformance
import movie.core.db.dao.MovieReferenceDaoThreading
import movie.core.db.dao.PosterDao
import movie.core.db.dao.PosterDaoPerformance
import movie.core.db.dao.PosterDaoThreading
import movie.core.db.dao.ShowingDao
import movie.core.db.dao.ShowingDaoLowercase
import movie.core.db.dao.ShowingDaoPerformance
import movie.core.db.dao.ShowingDaoThreading
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
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
            .addMigrations(MovieDatabase.Migration12to13())
            .addMigrations(MovieDatabase.Migration14to15())
            .build()
    }

    @Provides
    @Reusable
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
    @Reusable
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
    @Reusable
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
    @Reusable
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
    @Reusable
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
    @Reusable
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
    @Reusable
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
    @Reusable
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
    @Reusable
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
    @Reusable
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
    @Reusable
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

    @Provides
    @Reusable
    internal fun poster(
        database: MovieDatabase,
        tracer: PerformanceTracer
    ): PosterDao {
        var dao: PosterDao = database.poster()
        dao = PosterDaoPerformance(dao, tracer)
        dao = PosterDaoThreading(dao)
        return dao
    }

}