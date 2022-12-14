package movie.core.preference

interface EventPreference {

    var filterSeen: Boolean

    fun addListener(listener: OnChangedListener): OnChangedListener
    fun removeListener(listener: OnChangedListener)

    fun interface OnChangedListener {
        fun onChanged()
    }

}

