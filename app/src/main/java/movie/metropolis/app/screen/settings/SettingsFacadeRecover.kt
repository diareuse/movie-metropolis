package movie.metropolis.app.screen.settings

class SettingsFacadeRecover(
    private val origin: SettingsFacade
) : SettingsFacade by origin {

    override var filterSeen: Boolean
        get() = origin.runCatching { filterSeen }.getOrDefault(false)
        set(value) {
            origin.runCatching { filterSeen = value }
        }

    override suspend fun getCalendars(): Map<String, String> {
        return origin.runCatching { getCalendars() }.onFailure { it.printStackTrace() }
            .getOrDefault(emptyMap())
    }

    override fun selectCalendar(id: String?) {
        origin.runCatching { selectCalendar(id) }
    }

}