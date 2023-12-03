package movie.metropolis.app.presentation.settings

class SettingsFacadeReactive(
    private val origin: SettingsFacadeFromFeature
) : SettingsFacade by origin {

    override var filterSeen: Boolean
        get() = origin.filterSeen
        set(value) {
            val prev = filterSeen
            origin.filterSeen = value
            if (prev != filterSeen)
                notifyListeners()
        }

    override var onlyMovies: Boolean
        get() = origin.onlyMovies
        set(value) {
            val prev = onlyMovies
            origin.onlyMovies = value
            if (prev != onlyMovies)
                notifyListeners()
        }

    override var clipRadius: Int
        get() = origin.clipRadius
        set(value) {
            val prev = clipRadius
            origin.clipRadius = value
            if (prev != clipRadius)
                notifyListeners()
        }

    override var selectedCalendar: String?
        get() = origin.selectedCalendar
        set(value) {
            val prev = selectedCalendar
            origin.selectedCalendar = value
            if (prev != selectedCalendar)
                notifyListeners()
        }

    override var filters: List<String>
        get() = origin.filters
        set(value) {
            val prev = filters
            origin.filters = value
            if (prev != filters)
                notifyListeners()
        }

    private fun notifyListeners() = synchronized(origin.listeners) {
        for (listener in origin.listeners)
            listener.onChanged()
    }

}