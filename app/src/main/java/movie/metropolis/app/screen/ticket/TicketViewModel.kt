package movie.metropolis.app.screen.ticket

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.booking.BookingFacade.Companion.bookingsFlow
import movie.metropolis.app.util.retainStateIn
import movie.metropolis.app.util.writeTo
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val facade: BookingFacade,
    @ApplicationContext
    context: Context
) : ViewModel() {

    private val cacheDir by lazy { context.cacheDir }
        @WorkerThread get

    private val refreshFlow = Channel<Unit>(Channel.RENDEZVOUS)

    @Suppress("USELESS_CAST")
    val tickets = facade.bookingsFlow(refreshFlow.consumeAsFlow().map { { facade.refresh() } })
        .map { TicketContentState.Success(it.toImmutableList()) as TicketContentState }
        .catch { emit(TicketContentState.Failure(it)) }
        .retainStateIn(viewModelScope, TicketContentState.Loading)

    fun refresh() {
        refreshFlow.trySend(Unit)
    }

    suspend fun share(view: BookingView): File {
        val image = requireNotNull(facade.getShareImage(view))
        val dir = withContext(Dispatchers.IO) { cacheDir }
        return File(dir, "tickets/${view.movie.name.sanitize()}.png").apply {
            parentFile?.mkdirs()
            image.writeTo(this)
        }
    }

    private fun String.sanitize() = replace(Regex("\\s"), "-").lowercase()

}