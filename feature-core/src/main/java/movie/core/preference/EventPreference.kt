package movie.core.preference

interface EventPreference {

    var filterSeen: Boolean
    var onlyMovies: Boolean
    var calendarId: String?
    var distanceKms: Int
    var keywords: List<String>

    fun addOnChangedListener(listener: OnChangedListener): OnChangedListener
    fun removeOnChangedListener(listener: OnChangedListener): OnChangedListener

    fun interface OnChangedListener {
        fun onChanged()
    }

}