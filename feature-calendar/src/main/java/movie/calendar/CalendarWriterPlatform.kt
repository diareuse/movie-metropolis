package movie.calendar

import android.content.ContentResolver
import android.content.ContentValues
import android.provider.CalendarContract
import java.util.TimeZone

internal class CalendarWriterPlatform(
    private val resolver: ContentResolver,
    private val calendarId: String
) : CalendarWriter {

    override fun write(metadata: EventMetadata) {
        val values = ContentValues().apply {
            put(CalendarContract.Events.TITLE, metadata.name)
            put(CalendarContract.Events.DTSTART, metadata.start.time)
            put(CalendarContract.Events.DTEND, metadata.end.time)
            put(CalendarContract.Events.DESCRIPTION, metadata.description)
            put(CalendarContract.Events.EVENT_LOCATION, metadata.location)
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
        resolver.insert(CalendarContract.Events.CONTENT_URI, values)
    }

}