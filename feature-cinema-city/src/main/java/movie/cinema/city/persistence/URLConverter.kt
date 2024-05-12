package movie.cinema.city.persistence

import androidx.room.TypeConverter
import java.net.URL

internal class URLConverter {
    @TypeConverter
    fun convert(url: URL): String = url.toString()

    @TypeConverter
    fun convert(url: String): URL = URL(url)
}