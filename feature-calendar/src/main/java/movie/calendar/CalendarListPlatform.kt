package movie.calendar

import android.content.ContentResolver
import android.provider.CalendarContract.Calendars

internal class CalendarListPlatform(
    private val resolver: ContentResolver
) : CalendarList {

    override fun query(): List<CalendarMetadata> {
        val projection = arrayOf(
            Calendars._ID,
            Calendars.NAME,
            Calendars.ACCOUNT_NAME,
            Calendars.SYNC_EVENTS
        )
        val accounts = mutableListOf<CalendarMetadata>()
        val selection = "${Calendars.SYNC_EVENTS} = ?"
        resolver.query(
            Calendars.CONTENT_URI,
            projection,
            selection,
            arrayOf("1"),
            null
        )?.use {
            while (it.moveToNext()) {
                val account = it.getString(projection.indexOf(Calendars.ACCOUNT_NAME))
                val name = it.getString(projection.indexOf(Calendars.NAME))
                val id = it.getString(projection.indexOf(Calendars._ID))
                accounts += CalendarMetadata(id, "$name ($account)")
            }
        }
        return accounts
    }

}