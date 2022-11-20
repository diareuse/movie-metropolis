package movie.metropolis.app.screen.booking

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.LoadableList
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor() : ViewModel() {

    private val items: Flow<LoadableList<BookingView>> = TODO()
    val expired: Flow<LoadableList<BookingView>> = TODO()
    val active: Flow<LoadableList<BookingView>> = TODO()

}