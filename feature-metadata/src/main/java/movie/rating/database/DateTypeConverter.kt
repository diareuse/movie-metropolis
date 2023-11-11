package movie.rating.database

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {

    @TypeConverter
    fun from(date: Date) = date.time

    @TypeConverter
    fun to(time: Long) = Date(time)

}