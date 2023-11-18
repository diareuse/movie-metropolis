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
    version = 16,
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
        AutoMigration(13, 14)
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
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `movie_media_copy` (`movie` TEXT NOT NULL, `width` INTEGER, `height` INTEGER, `url` TEXT NOT NULL, `type` TEXT NOT NULL, PRIMARY KEY(`url`), FOREIGN KEY(`movie`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            db.execSQL("insert into movie_media_copy (`movie`,`width`,`height`,`url`,`type`) select `movie`,`width`,`height`,`url`,`type` from movie_media")
            db.execSQL("drop table movie_media")
            db.execSQL("alter table movie_media_copy rename to movie_media")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_movie_media_movie` ON `movie_media` (`movie`)")
        }
    }

    class Migration7to8 : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // --- table
            db.execSQL("CREATE TABLE IF NOT EXISTS `movie_previews_copy` (`movie` TEXT NOT NULL, `screening_from` INTEGER NOT NULL, `description` TEXT NOT NULL, `directors` TEXT NOT NULL, `cast` TEXT NOT NULL, `country_of_origin` TEXT NOT NULL, `upcoming` INTEGER NOT NULL, `genres` TEXT NOT NULL, PRIMARY KEY(`movie`), FOREIGN KEY(`movie`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            db.execSQL("insert into movie_previews_copy (`movie`, `screening_from`, `description`, `directors`, `cast`, `country_of_origin`, `upcoming`, `genres`) select `movie`, `screening_from`, `description`, `directors`, `cast`, `country_of_origin`, `upcoming`, \"\" from movie_previews")
            db.execSQL("drop table movie_previews")
            db.execSQL("alter table movie_previews_copy rename to movie_previews")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_movie_previews_movie` ON `movie_previews` (`movie`)")
            // --- view
            db.execSQL("drop view `movie_preview_views`")
            db.execSQL("CREATE VIEW `movie_preview_views` AS select movies.id,movies.name,movies.url,movies.released_at,movies.duration,movie_previews.screening_from,movie_previews.description,movie_previews.directors,movie_previews.`cast`,movie_previews.country_of_origin,movie_previews.upcoming,movie_previews.genres from movies, movie_previews where movies.id=movie_previews.movie")
        }
    }

    class Migration10to11 : Migration(10, 11) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `movie_ratings_copy` (`movie` TEXT NOT NULL, `rating` INTEGER NOT NULL, `link_imdb` TEXT, `link_rt` TEXT, `link_csfd` TEXT, `created_at` INTEGER NOT NULL, PRIMARY KEY(`movie`), FOREIGN KEY(`movie`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            db.execSQL("insert into `movie_ratings_copy` (`movie`,`rating`,`link_imdb`,`link_rt`,`link_csfd`,`created_at`) select `movie`,`rating`,`link_imdb`,`link_rt`,`link_csfd`,0 from movie_ratings")
            db.execSQL("drop table `movie_ratings`")
            db.execSQL("alter table `movie_ratings_copy` rename to `movie_ratings`")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_movie_ratings_movie` ON `movie_ratings` (`movie`)")
        }
    }

    @DeleteTable.Entries(
        DeleteTable(
            tableName = "movie_ratings"
        )
    )
    class Migration11to12 : AutoMigrationSpec

    class Migration12to13 : Migration(12, 13) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("delete from `showings`")
            db.execSQL("alter table `showings` add column `subtitles` text")
        }
    }

    class Migration14to15 : Migration(14, 15) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `movie_details_copy` (`movie` TEXT NOT NULL, `original_name` TEXT NOT NULL, `country_of_origin` TEXT, `cast` TEXT NOT NULL, `directors` TEXT NOT NULL, `description` TEXT NOT NULL, `screening_from` INTEGER NOT NULL, `age_restriction_url` TEXT NOT NULL, `genres` TEXT NOT NULL, PRIMARY KEY(`movie`), FOREIGN KEY(`movie`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            db.execSQL("insert into `movie_details_copy` (`movie`,`original_name`,`country_of_origin`,`cast`,`directors`,`description`,`screening_from`,`age_restriction_url`,`genres`) select `movie`,`original_name`,`country_of_origin`,`cast`,`directors`,`description`,`screening_from`,`age_restriction_url`,'' from `movie_details`")
            db.execSQL("drop table `movie_details`")
            db.execSQL("alter table `movie_details_copy` rename to `movie_details`")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_movie_details_movie` ON `movie_details` (`movie`)")
            db.execSQL("drop view `movie_detail_views`")
            db.execSQL("CREATE VIEW `movie_detail_views` AS select m.id, m.name, m.url, m.released_at, m.duration, md.original_name, md.country_of_origin, md.`cast`, md.directors, mp.description, md.screening_from, md.age_restriction_url, md.genres from movies as m, movie_details as md, movie_previews as mp where md.movie=m.id and mp.movie=md.movie")
        }
    }

    class Migration15to16 : Migration(15, 16) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `movie_favorites_copy` (`movie` TEXT NOT NULL, `notified` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, PRIMARY KEY(`movie`), FOREIGN KEY(`movie`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            val now = System.currentTimeMillis()
            db.execSQL("insert into `movie_favorites_copy` (`movie`, `notified`, `created_at`) select mf.`movie`, (m.`screening_from` < $now) as `notified`, mf.`created_at` from `movie_favorites` as mf join `movie_details` as m on m.`movie`=mf.`movie`")
            db.execSQL("drop table `movie_favorites`")
            db.execSQL("alter table `movie_favorites_copy` rename to `movie_favorites`")
            // ---
            db.execSQL("CREATE TABLE IF NOT EXISTS `movies_copy` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `url` TEXT NOT NULL, `released_at` INTEGER NOT NULL, `screening_from` INTEGER NOT NULL, `duration` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            db.execSQL("insert into `movies_copy` (`id`,`name`,`url`,`released_at`,`screening_from`,`duration`) select `id`, `name`, `url`, `released_at`, COALESCE((select `screening_from` from `movie_details` as md where md.`movie`= m.`id`),(select `screening_from` from `movie_previews` as mp where mp.`movie`=m.`id`),0), `duration` from `movies` as m")
            db.execSQL("drop table `movies`")
            db.execSQL("alter table `movies_copy` rename to `movies`")
            // ---
            db.execSQL("drop view `movie_reference_views`")
            db.execSQL("CREATE VIEW `movie_reference_views` AS select m.id,m.name,m.url,m.released_at,m.screening_from,m.duration,mr.poster,mr.video from movies as m, movie_references as mr where m.id=mr.movie")
        }
    }

}