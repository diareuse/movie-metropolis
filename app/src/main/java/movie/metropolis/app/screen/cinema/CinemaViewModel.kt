package movie.metropolis.app.screen.cinema

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.LoadableList
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CinemaViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val selectedDate = MutableStateFlow(Date())
    val items: Flow<LoadableList<MovieBookingView>> = TODO()

}