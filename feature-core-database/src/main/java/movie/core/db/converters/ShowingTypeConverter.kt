package movie.core.db.converters

import androidx.room.TypeConverter

class ShowingTypeConverter : BaseConverter<List<String>, String> {

    @TypeConverter
    override fun convertFrom(from: List<String>): String {
        return from.joinToString(separator = " | ")
    }

    @TypeConverter
    override fun convertTo(from: String): List<String> {
        return from.split(" | ")
    }

}