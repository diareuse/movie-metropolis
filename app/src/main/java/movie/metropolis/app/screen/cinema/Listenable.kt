package movie.metropolis.app.screen.cinema

import java.util.Collections

class Listenable<Listener> {

    private val listeners = Collections.synchronizedSet(mutableSetOf<Listener>())

    operator fun plusAssign(listener: Listener) {
        listeners += listener
    }

    operator fun minusAssign(listener: Listener) {
        listeners -= listener
    }

    fun notify(body: Listener.() -> Unit) {
        synchronized(listeners) {
            for (listener in listeners) {
                listener.run(body)
            }
        }
    }

}