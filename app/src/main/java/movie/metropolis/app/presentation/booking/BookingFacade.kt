package movie.metropolis.app.presentation.booking

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.BookingView

@Stable
interface BookingFacade {

    val bookings: Flow<List<BookingView>>

}