package movie.metropolis.app.feature.global.serializer

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

internal class TimestampSerializer : KDateSerializer() {

    override val type: String = "timestamp+timezone"
    override val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

}