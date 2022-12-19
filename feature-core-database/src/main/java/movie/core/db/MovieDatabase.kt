package movie.core.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import movie.core.db.converters.DateConverter
import movie.core.db.converters.IterableStringConverter
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieFavoriteDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieRatingDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.db.model.BookingSeatsStored
import movie.core.db.model.BookingStored
import movie.core.db.model.CinemaStored
import movie.core.db.model.MediaTypeConverter
import movie.core.db.model.MovieDetailStored
import movie.core.db.model.MovieDetailView
import movie.core.db.model.MovieFavoriteStored
import movie.core.db.model.MovieMediaStored
import movie.core.db.model.MoviePreviewStored
import movie.core.db.model.MoviePreviewView
import movie.core.db.model.MovieRatingStored
import movie.core.db.model.MovieReferenceStored
import movie.core.db.model.MovieReferenceView
import movie.core.db.model.MovieStored
import movie.core.db.model.ShowingStored

@Database(
    version = 4,
    entities = [
        BookingStored::class,
        BookingSeatsStored::class,
        CinemaStored::class,
        MovieStored::class,
        MovieMediaStored::class,
        MovieDetailStored::class,
        MoviePreviewStored::class,
        MovieReferenceStored::class,
        ShowingStored::class,
        MovieFavoriteStored::class,
        MovieRatingStored::class
    ],
    views = [
        MovieDetailView::class,
        MovieReferenceView::class,
        MoviePreviewView::class
    ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(3, 4),
    ]
)
@TypeConverters(
    DateConverter::class,
    IterableStringConverter::class,
    MediaTypeConverter::class
)
internal abstract class MovieDatabase : RoomDatabase() {

    abstract fun booking(): BookingDao
    abstract fun bookingSeats(): BookingSeatsDao
    abstract fun cinema(): CinemaDao
    abstract fun movie(): MovieDao
    abstract fun movieDetail(): MovieDetailDao
    abstract fun movieMedia(): MovieMediaDao
    abstract fun showing(): ShowingDao
    abstract fun movieReference(): MovieReferenceDao
    abstract fun moviePreview(): MoviePreviewDao
    abstract fun movieFavorite(): MovieFavoriteDao
    abstract fun movieRating(): MovieRatingDao

    class Migration2to3 : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `movie_media_copy` (`movie` TEXT NOT NULL, `width` INTEGER, `height` INTEGER, `url` TEXT NOT NULL, `type` TEXT NOT NULL, PRIMARY KEY(`url`), FOREIGN KEY(`movie`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            database.execSQL("insert into movie_media_copy (`movie`,`width`,`height`,`url`,`type`) select `movie`,`width`,`height`,`url`,`type` from movie_media")
            database.execSQL("drop table movie_media")
            database.execSQL("alter table movie_media_copy rename to movie_media")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_movie_media_movie` ON `movie_media` (`movie`)")
        }
    }

}