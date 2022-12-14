package movie.settings

import movie.settings.ObservablePreference.OnKeyChanged
import java.util.Collections

class ObservablePreferenceDefault : ObservablePreference {

    private val listeners = Collections.synchronizedSet(mutableSetOf<OnKeyChanged>())

    override fun addListener(listener: OnKeyChanged): OnKeyChanged {
        listeners += listener
        return listener
    }

    override fun removeListener(listener: OnKeyChanged) {
        listeners -= listener
    }

    override fun notify(key: String) {
        synchronized(listeners) {
            for (listener in listeners) {
                listener.onChanged(key)
            }
        }
    }

}