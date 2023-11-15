package movie.metropolis.app.screen.ticket

import androidx.compose.runtime.*
import kotlinx.collections.immutable.ImmutableList
import movie.metropolis.app.model.BookingView

@Immutable
sealed class TicketContentState {

    val isSuccess get() = this is Success
    val isFailure get() = this is Failure
    val isLoading get() = this is Loading

    fun asSuccess() = this as? Success
    fun asFailure() = this as? Failure
    fun asLoading() = this as? Loading

    data class Success(val items: ImmutableList<BookingView>) : TicketContentState(),
        ImmutableList<BookingView> by items

    data class Failure(val error: Throwable) : TicketContentState()
    data object Loading : TicketContentState()
}