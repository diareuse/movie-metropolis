package movie.core.nwk.serializer

import java.text.SimpleDateFormat
import java.util.Locale

internal class DateSerializer : KDateSerializer() {

    override val type = "date"
    override val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

}