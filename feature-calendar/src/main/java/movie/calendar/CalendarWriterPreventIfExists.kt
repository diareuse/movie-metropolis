package movie.calendar

import android.content.ContentResolver
import android.provider.CalendarContract

class CalendarWriterPreventIfExists(
    private val origin: CalendarWriter,
    private val resolver: ContentResolver
) : CalendarWriter {

    override fun write(metadata: EventMetadata) {
        val projection = arrayOf(
            CalendarContract.Events._ID
        )
        val selection =
            "(${CalendarContract.Instances.BEGIN} = ?) AND (${CalendarContract.Instances.END} = ?)"
        val args = arrayOf(
            metadata.start.time.toString(),
            metadata.end.time.toString()
        )
        resolver.query(CalendarContract.Instances.CONTENT_URI, projection, selection, args, null)
            ?.use {
                if (it.count > 0) return
            }
        origin.write(metadata)
    }

}