package movie.metropolis.app.presentation.settings

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.calendar.CalendarList
import movie.core.preference.EventPreference
import movie.core.preference.SyncPreference
import movie.metropolis.app.model.Calendars
import movie.metropolis.app.model.adapter.CalendarViewFromFeature
import movie.metropolis.app.presentation.OnChangedListener
import java.util.Collections
import java.util.Date
import kotlin.math.absoluteValue

class SettingsFacadeFromFeature(
    private val prefs: EventPreference,
    private val syncs: SyncPreference,
    private val calendars: CalendarList
) : SettingsFacade {

    val listeners: MutableSet<OnChangedListener> =
        Collections.synchronizedSet(mutableSetOf<OnChangedListener>())

    override var filterSeen: Boolean
        get() = prefs.filterSeen
        set(value) {
            prefs.filterSeen = value
        }

    override var onlyMovies: Boolean
        get() = prefs.onlyMovies
        set(value) {
            prefs.onlyMovies = value
        }

    override val addToCalendar: Boolean
        get() = prefs.calendarId != null

    override var clipRadius: Int
        get() = prefs.distanceKms
        set(value) {
            prefs.distanceKms = value.absoluteValue.coerceAtMost(20037)
        }

    override var selectedCalendar: String?
        get() = prefs.calendarId
        set(value) {
            prefs.calendarId = value
        }

    override var filters: List<String>
        get() = prefs.keywords
        set(value) {
            prefs.keywords = value
        }

    override suspend fun getCalendars(): Calendars {
        return calendars.query().map(::CalendarViewFromFeature).let(::Calendars)
    }

    override suspend fun cleanTimestamps() = withContext(Dispatchers.IO) {
        syncs.booking = Date(0)
        syncs.cinema = Date(0)
        syncs.previewCurrent = Date(0)
        syncs.previewUpcoming = Date(0)
    }

    override fun addListener(listener: OnChangedListener): OnChangedListener {
        listeners += listener
        return listener
    }

    override fun removeListener(listener: OnChangedListener) {
        listeners -= listener
    }

}

