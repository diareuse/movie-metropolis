package movie.cinema.city.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(URLConverter::class, DateConverter::class)
@Database(
    entities = [
        MovieStored::class,
        MovieStored.Cast::class,
        MovieStored.Director::class,
        MovieStored.Genre::class,
        MovieStored.Video::class,
        MovieStored.Image::class,
        TicketStored::class,
        TicketStored.Reservation::class,
    ],
    version = 1
)
internal abstract class MovieStorage : RoomDatabase() {
    abstract fun movie(): MovieDao
    abstract fun tickets(): TicketDao
}