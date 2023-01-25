package movie.metropolis.app.screen.settings

import movie.calendar.CalendarList
import movie.core.preference.EventPreference
import movie.metropolis.app.screen.OnChangedListener
import java.util.Collections
import kotlin.math.absoluteValue

class SettingsFacadeFromFeature(
    private val prefs: EventPreference,
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

    override suspend fun getCalendars(): Map<String, String> {
        return calendars.query().associate { it.id to it.name }
    }

    override fun selectCalendar(id: String?) {
        prefs.calendarId = id
    }

    override fun addListener(listener: OnChangedListener): OnChangedListener {
        listeners += listener
        return listener
    }

    override fun removeListener(listener: OnChangedListener) {
        listeners -= listener
    }

}

