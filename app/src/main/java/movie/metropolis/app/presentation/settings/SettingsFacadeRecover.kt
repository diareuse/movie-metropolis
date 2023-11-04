package movie.metropolis.app.presentation.settings

import movie.log.logSevere
import movie.metropolis.app.model.Calendars

class SettingsFacadeRecover(
    private val origin: SettingsFacade
) : SettingsFacade by origin {

    override var filterSeen: Boolean
        get() = origin.runCatching { filterSeen }.logSevere().getOrDefault(false)
        set(value) {
            origin.runCatching { filterSeen = value }.logSevere()
        }

    override var onlyMovies: Boolean
        get() = origin.runCatching { onlyMovies }.logSevere().getOrDefault(false)
        set(value) {
            origin.runCatching { onlyMovies = value }.logSevere()
        }

    override val addToCalendar: Boolean
        get() = origin.runCatching { addToCalendar }.logSevere().getOrDefault(false)

    override var clipRadius: Int
        get() = origin.runCatching { clipRadius }.logSevere().getOrDefault(0)
        set(value) {
            origin.runCatching { clipRadius = value }.logSevere()
        }

    override var selectedCalendar: String?
        get() = origin.runCatching { selectedCalendar }.logSevere().getOrNull()
        set(value) {
            origin.runCatching { selectedCalendar = value }.logSevere()
        }

    override suspend fun getCalendars() = origin
        .runCatching { getCalendars() }
        .logSevere()
        .getOrDefault(Calendars(emptyList()))

}