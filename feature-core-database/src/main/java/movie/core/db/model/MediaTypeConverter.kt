package movie.core.db.model

import androidx.room.TypeConverter
import movie.core.db.converters.BaseConverter

class MediaTypeConverter : BaseConverter<MovieMediaStored.Type, String> {

    @TypeConverter
    override fun convertFrom(from: MovieMediaStored.Type): String {
        return from.type
    }

    @TypeConverter
    override fun convertTo(from: String): MovieMediaStored.Type {
        return MovieMediaStored.Type.values().firstOrNull { it.type == from }
            ?: MovieMediaStored.Type.Video
    }

}