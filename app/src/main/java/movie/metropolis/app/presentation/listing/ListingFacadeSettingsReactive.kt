package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import movie.core.preference.EventPreference

class ListingFacadeSettingsReactive(
    private val origin: ListingFacade,
    private val settings: EventPreference
) : ListingFacade by origin {

    override fun get() = settings.asFlow().flatMapLatest { origin.get() }

    private fun EventPreference.asFlow() = callbackFlow {
        send(Unit)
        val listener = addOnChangedListener {
            trySend(Unit)
        }
        awaitClose {
            removeOnChangedListener(listener)
        }
    }

}