package movie.metropolis.app.presentation.settings

import kotlinx.coroutines.flow.MutableStateFlow
import movie.calendar.CalendarList
import movie.metropolis.app.model.Calendars
import movie.metropolis.app.model.adapter.CalendarViewFromFeature
import movie.settings.GlobalPreferences

class SettingsFacadeFromFeature(
    private val prefs: GlobalPreferences,
    private val calendars: CalendarList
) : SettingsFacade {

    override val filterSeen = MutableStateFlow(prefs.filterSeen)
    override val onlyMovies = MutableStateFlow(prefs.onlyMovies)
    override val addToCalendar = MutableStateFlow(prefs.calendarId != null)
    override val clipRadius = MutableStateFlow(prefs.distanceKms)
    override val selectedCalendar = MutableStateFlow(prefs.calendarId)
    override val filters = MutableStateFlow(prefs.keywords.toList())

    override suspend fun getCalendars(): Calendars {
        return calendars.query().map(::CalendarViewFromFeature).let(::Calendars)
    }

    override fun setFilterSeen(value: Boolean) {
        prefs.filterSeen = value.apply { filterSeen.value = this }
    }

    override fun setOnlyMovies(value: Boolean) {
        prefs.onlyMovies = value.apply { onlyMovies.value = this }
    }

    override fun setClipRadius(value: Int) {
        prefs.distanceKms = value.apply { clipRadius.value = this }
    }

    override fun setSelectedCalendar(value: String?) {
        prefs.calendarId = value.apply {
            selectedCalendar.value = this
            addToCalendar.value = this != null
        }
    }

    override fun setFilters(value: List<String>) {
        prefs.keywords = value.apply { filters.value = this }.toSet()
    }

}