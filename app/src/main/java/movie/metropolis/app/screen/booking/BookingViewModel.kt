package movie.metropolis.app.screen.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.booking.BookingFacade.Companion.bookingsFlow
import movie.metropolis.app.screen.mapLoadable
import movie.metropolis.app.screen.retainStateIn
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    facade: BookingFacade
) : ViewModel() {

    private val items = facade.bookingsFlow
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)
    val expired = items
        .mapLoadable { it.filterIsInstance<BookingView.Expired>() }
        .retainStateIn(viewModelScope, Loadable.loading())
    val active = items
        .mapLoadable { it.filterIsInstance<BookingView.Active>() }
        .retainStateIn(viewModelScope, Loadable.loading())

}