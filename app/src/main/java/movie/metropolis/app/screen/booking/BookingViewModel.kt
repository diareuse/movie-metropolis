package movie.metropolis.app.screen.booking

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.booking.BookingFacade.Companion.bookingsFlow
import movie.metropolis.app.presentation.share.ShareFacade
import movie.metropolis.app.presentation.share.TicketRepresentation
import movie.metropolis.app.util.retainStateIn
import movie.metropolis.app.util.writeTo
import java.io.File
import javax.inject.Inject

@Stable
@HiltViewModel
class BookingViewModel @Inject constructor(
    private val facade: BookingFacade,
    private val share: ShareFacade,
    @ApplicationContext
    context: Context
) : ViewModel() {

    private val cacheDir by lazy { context.cacheDir }
        @WorkerThread get

    private val refreshToken = Channel<suspend () -> Unit>()
    private val refreshTokenFlow = refreshToken.consumeAsFlow()
        .shareIn(viewModelScope, SharingStarted.Eagerly, 0)
    private val items = facade.bookingsFlow(refreshTokenFlow)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)
    val expired = items
        .map { it.filter { it.expired } }
        .map { it.toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())
    val active = items
        .map { it.filterNot { it.expired } }
        .map { it.toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())

    fun refresh() {
        refreshToken.trySend(facade::refresh)
    }

    fun saveTicket(ticket: TicketRepresentation) {
        viewModelScope.launch {
            share.putTicket(ticket)
        }
    }

    suspend fun saveAsFile(booking: BookingView): File {
        val image = facade.getShareImage(booking)
        val dir = withContext(Dispatchers.IO) { cacheDir }
        return File(dir, "tickets/ticket.png").apply {
            parentFile?.mkdirs()
            image?.writeTo(this)
        }
    }

}