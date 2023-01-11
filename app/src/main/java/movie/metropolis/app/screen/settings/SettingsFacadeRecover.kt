package movie.metropolis.app.screen.settings

import movie.log.logSevere

class SettingsFacadeRecover(
    private val origin: SettingsFacade
) : SettingsFacade by origin {

    override var filterSeen: Boolean
        get() = origin.runCatching { filterSeen }.logSevere().getOrDefault(false)
        set(value) {
            origin.runCatching { filterSeen = value }.logSevere()
        }

    override suspend fun getCalendars(): Map<String, String> = origin
        .runCatching { getCalendars() }
        .logSevere()
        .getOrDefault(emptyMap())

    override fun selectCalendar(id: String?) {
        origin.runCatching { selectCalendar(id) }.logSevere()
    }

}