package movie.cinema.city.serializer

import java.text.SimpleDateFormat
import java.util.Locale

internal class TimestampSerializer : KDateSerializer() {

    override val type: String = "timestamp+timezone"
    override val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())

}