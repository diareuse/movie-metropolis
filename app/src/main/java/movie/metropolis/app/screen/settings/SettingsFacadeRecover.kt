package movie.metropolis.app.screen.settings

import movie.log.logCatching

class SettingsFacadeRecover(
    private val origin: SettingsFacade
) : SettingsFacade by origin {

    override var filterSeen: Boolean
        get() = origin.logCatching("filter-seen") { filterSeen }.getOrDefault(false)
        set(value) {
            origin.logCatching("filter-seen") { filterSeen = value }
        }

    override suspend fun getCalendars(): Map<String, String> = origin
        .logCatching("calendars") { getCalendars() }
        .getOrDefault(emptyMap())

    override fun selectCalendar(id: String?) {
        origin.logCatching("calendar") { selectCalendar(id) }
    }

}