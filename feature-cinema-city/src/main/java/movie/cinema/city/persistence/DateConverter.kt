package movie.cinema.city.persistence

import androidx.room.TypeConverter
import java.util.Date

internal class DateConverter {
    @TypeConverter
    fun convert(date: Long): Date = Date(date)

    @TypeConverter
    fun convert(date: Date): Long = date.time
}