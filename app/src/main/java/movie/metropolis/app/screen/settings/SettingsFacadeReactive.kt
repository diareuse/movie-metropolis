package movie.metropolis.app.screen.settings

class SettingsFacadeReactive(
    private val origin: SettingsFacadeFromFeature
) : SettingsFacade by origin {

    override var filterSeen: Boolean
        get() = origin.filterSeen
        set(value) {
            origin.filterSeen = value
            synchronized(origin.listeners) {
                for (listener in origin.listeners)
                    listener.onChanged()
            }
        }

}