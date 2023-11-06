package movie.core.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import movie.core.db.MovieDatabase.Migration11to12
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
import movie.core.db.dao.MoviePromoDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.PosterDao
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
import movie.core.db.model.MoviePromoStored
import movie.core.db.model.MovieReferenceStored
import movie.core.db.model.MovieReferenceView
import movie.core.db.model.MovieStored
import movie.core.db.model.ShowingStored

@Database(
    version = 13,
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
        MoviePromoStored::class
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
        AutoMigration(4, 5),
        AutoMigration(5, 6),
        AutoMigration(6, 7),
        AutoMigration(8, 9),
        AutoMigration(9, 10),
        AutoMigration(11, 12, Migration11to12::class),
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
    abstract fun moviePromo(): MoviePromoDao
    abstract fun poster(): PosterDao

    class Migration2to3 : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `movie_media_copy` (`movie` TEXT NOT NULL, `width` INTEGER, `height` INTEGER, `url` TEXT NOT NULL, `type` TEXT NOT NULL, PRIMARY KEY(`url`), FOREIGN KEY(`movie`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            database.execSQL("insert into movie_media_copy (`movie`,`width`,`height`,`url`,`type`) select `movie`,`width`,`height`,`url`,`type` from movie_media")
            database.execSQL("drop table movie_media")
            database.execSQL("alter table movie_media_copy rename to movie_media")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_movie_media_movie` ON `movie_media` (`movie`)")
        }
    }

    class Migration7to8 : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // --- table
            database.execSQL("CREATE TABLE IF NOT EXISTS `movie_previews_copy` (`movie` TEXT NOT NULL, `screening_from` INTEGER NOT NULL, `description` TEXT NOT NULL, `directors` TEXT NOT NULL, `cast` TEXT NOT NULL, `country_of_origin` TEXT NOT NULL, `upcoming` INTEGER NOT NULL, `genres` TEXT NOT NULL, PRIMARY KEY(`movie`), FOREIGN KEY(`movie`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            database.execSQL("insert into movie_previews_copy (`movie`, `screening_from`, `description`, `directors`, `cast`, `country_of_origin`, `upcoming`, `genres`) select `movie`, `screening_from`, `description`, `directors`, `cast`, `country_of_origin`, `upcoming`, \"\" from movie_previews")
            database.execSQL("drop table movie_previews")
            database.execSQL("alter table movie_previews_copy rename to movie_previews")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_movie_previews_movie` ON `movie_previews` (`movie`)")
            // --- view
            database.execSQL("drop view `movie_preview_views`")
            database.execSQL("CREATE VIEW `movie_preview_views` AS select movies.id,movies.name,movies.url,movies.released_at,movies.duration,movie_previews.screening_from,movie_previews.description,movie_previews.directors,movie_previews.`cast`,movie_previews.country_of_origin,movie_previews.upcoming,movie_previews.genres from movies, movie_previews where movies.id=movie_previews.movie")
        }
    }

    class Migration10to11 : Migration(10, 11) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `movie_ratings_copy` (`movie` TEXT NOT NULL, `rating` INTEGER NOT NULL, `link_imdb` TEXT, `link_rt` TEXT, `link_csfd` TEXT, `created_at` INTEGER NOT NULL, PRIMARY KEY(`movie`), FOREIGN KEY(`movie`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            database.execSQL("insert into `movie_ratings_copy` (`movie`,`rating`,`link_imdb`,`link_rt`,`link_csfd`,`created_at`) select `movie`,`rating`,`link_imdb`,`link_rt`,`link_csfd`,0 from movie_ratings")
            database.execSQL("drop table `movie_ratings`")
            database.execSQL("alter table `movie_ratings_copy` rename to `movie_ratings`")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_movie_ratings_movie` ON `movie_ratings` (`movie`)")
        }
    }

    @DeleteTable.Entries(
        DeleteTable(
            tableName = "movie_ratings"
        )
    )
    class Migration11to12 : AutoMigrationSpec

}