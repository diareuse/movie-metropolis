package movie.metropolis.app.screen.booking

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import movie.metropolis.app.model.TimeView

sealed class BookingState {
    data class Value(
        val value: List<TimeView>
    ) : BookingState()

    data object Loading : BookingState()
    data object Empty : BookingState()
    data class Error(
        val exception: Throwable
    ) : BookingState()

    companion object {
        fun loading() = Loading as BookingState

        fun error(exception: Throwable) = Error(exception) as BookingState

        fun Flow<List<TimeView>>.asBookingState() =
            map { if (it.all { it.times.isEmpty() }) Empty else Value(it) }
                .onStart { emit(loading()) }
    }
}