package movie.core.db.converters

import androidx.room.TypeConverter

class IterableStringConverter : BaseConverter<Iterable<String>, String> {

    @TypeConverter
    override fun convertFrom(from: Iterable<String>): String {
        return from.joinToString(separator = "<|>")
    }

    @TypeConverter
    override fun convertTo(from: String): Iterable<String> {
        return from.split("<|>").filterNot { it.isBlank() }
    }

}