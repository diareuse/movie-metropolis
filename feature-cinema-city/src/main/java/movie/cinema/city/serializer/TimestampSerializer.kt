package movie.cinema.city.serializer

import android.os.Build
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

internal class TimestampSerializer : KDateSerializer() {

    override val type: String = "timestamp+timezone"
    override val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ROOT)
    } else {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

}