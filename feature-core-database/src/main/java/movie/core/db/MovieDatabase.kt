package movie.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import movie.core.db.converters.DateConverter
import movie.core.db.converters.IterableStringConverter
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.db.model.BookingSeatsStored
import movie.core.db.model.BookingStored
import movie.core.db.model.CinemaStored
import movie.core.db.model.MediaTypeConverter
import movie.core.db.model.MovieDetailStored
import movie.core.db.model.MovieDetailView
import movie.core.db.model.MovieMediaStored
import movie.core.db.model.MoviePreviewStored
import movie.core.db.model.MoviePreviewView
import movie.core.db.model.MovieReferenceStored
import movie.core.db.model.MovieReferenceView
import movie.core.db.model.MovieStored
import movie.core.db.model.ShowingStored

@Database(
    version = 1,
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
    ],
    views = [
        MovieDetailView::class,
        MovieReferenceView::class,
        MoviePreviewView::class
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

}