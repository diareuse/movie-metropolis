package movie.settings

interface ObservablePreference {

    fun addListener(listener: OnKeyChanged): OnKeyChanged
    fun removeListener(listener: OnKeyChanged)
    fun notify(key: String)

    fun interface OnKeyChanged {
        fun onChanged(key: String)
    }

}