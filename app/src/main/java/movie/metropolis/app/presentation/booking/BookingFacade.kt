package movie.metropolis.app.presentation.booking

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image

@Stable
interface BookingFacade {

    val bookings: Flow<List<BookingView>>

    suspend fun getShareImage(view: BookingView): Image

}