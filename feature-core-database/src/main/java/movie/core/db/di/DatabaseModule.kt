package movie.core.db.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import movie.core.db.MovieDatabase
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao

@Module
@InstallIn(ActivityRetainedComponent::class)
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
    ): BookingDao = database.booking()

    @Provides
    internal fun bookingSeats(
        database: MovieDatabase
    ): BookingSeatsDao = database.bookingSeats()

    @Provides
    internal fun cinema(
        database: MovieDatabase
    ): CinemaDao = database.cinema()

    @Provides
    internal fun movie(
        database: MovieDatabase
    ): MovieDao = database.movie()

    @Provides
    internal fun movieDetail(
        database: MovieDatabase
    ): MovieDetailDao = database.movieDetail()

    @Provides
    internal fun movieMedia(
        database: MovieDatabase
    ): MovieMediaDao = database.movieMedia()

    @Provides
    internal fun showing(
        database: MovieDatabase
    ): ShowingDao = database.showing()

    @Provides
    internal fun movieReference(
        database: MovieDatabase
    ): MovieReferenceDao = database.movieReference()

    @Provides
    internal fun moviePreview(
        database: MovieDatabase
    ): MoviePreviewDao = database.moviePreview()

}