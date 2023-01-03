package movie.metropolis.app.screen.booking

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.booking.BookingFacade.Companion.bookingsFlow
import movie.metropolis.app.screen.mapLoadable
import movie.metropolis.app.screen.retainStateIn
import movie.metropolis.app.screen.share.ShareFacade
import movie.metropolis.app.screen.share.TicketRepresentation
import movie.metropolis.app.util.writeTo
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val facade: BookingFacade,
    private val share: ShareFacade,
    @ApplicationContext
    context: Context
) : ViewModel() {

    private val cacheDir = context.cacheDir

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

    fun saveTicket(ticket: TicketRepresentation) {
        viewModelScope.launch {
            share.putTicket(ticket)
        }
    }

    suspend fun saveAsFile(booking: BookingView.Active): File {
        val image = facade.getImage(booking)
        return File(cacheDir, "tickets/ticket.png").apply {
            parentFile?.mkdirs()
            image?.writeTo(this)
        }
    }

}