package movie.metropolis.app.screen.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.booking.BookingFacade.Companion.bookingsFlow
import movie.metropolis.app.screen.mapLoadable
import movie.metropolis.app.screen.retainStateIn
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val facade: BookingFacade
) : ViewModel() {

    private val refreshToken = Channel<suspend () -> Unit>()
    private val items = facade.bookingsFlow(refreshToken.receiveAsFlow())
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)
    val expired = items
        .mapLoadable { it.filterIsInstance<BookingView.Expired>() }
        .retainStateIn(viewModelScope, Loadable.loading())
    val active = items
        .mapLoadable { it.filterIsInstance<BookingView.Active>() }
        .retainStateIn(viewModelScope, Loadable.loading())

    fun refresh() {
        refreshToken.trySend(facade::refresh)
    }

    suspend fun saveAsFile(booking: BookingView.Active): File {
        return facade.saveAsFile(booking)
    }

}