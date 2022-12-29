package movie.calendar

import android.content.ContentResolver
import android.provider.CalendarContract

class CalendarListPlatform(
    private val resolver: ContentResolver
) : CalendarList {

    override fun query(): List<CalendarMetadata> {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.SYNC_EVENTS
        )
        val accounts = mutableListOf<CalendarMetadata>()
        val selection = "${CalendarContract.Calendars.SYNC_EVENTS} = ?"
        resolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            arrayOf("1"),
            null
        )?.use {
            while (it.moveToNext()) {
                val account =
                    it.getString(projection.indexOf(CalendarContract.Calendars.ACCOUNT_NAME))
                val id = it.getString(projection.indexOf(CalendarContract.Calendars._ID))
                accounts += CalendarMetadata(id, account)
            }
        }
        return accounts
    }

}