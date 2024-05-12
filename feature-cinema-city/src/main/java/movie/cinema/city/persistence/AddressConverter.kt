package movie.cinema.city.persistence

import androidx.room.TypeConverter

class AddressConverter {
    @TypeConverter
    fun convert(lines: List<String>) = lines.joinToString(System.lineSeparator())

    @TypeConverter
    fun convert(lines: String) = lines.split(System.lineSeparator())
}