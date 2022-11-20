package movie.metropolis.app.screen.cinema

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.LocationSnapshot
import movie.metropolis.app.screen.LoadableList
import javax.inject.Inject

@HiltViewModel
class CinemasViewModel @Inject constructor() : ViewModel() {

    val location = MutableStateFlow(null as LocationSnapshot?)
    val items: Flow<LoadableList<CinemaView>> = TODO()

}