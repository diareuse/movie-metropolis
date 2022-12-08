package movie.core.db.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter : BaseConverter<Date, Long> {

    @TypeConverter
    override fun convertFrom(from: Date): Long {
        return from.time
    }

    @TypeConverter
    override fun convertTo(from: Long): Date {
        return Date(from)
    }

}