package movie.metropolis.app.screen.listing

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.LoadableList
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor() : ViewModel() {

    val current: Flow<LoadableList<MovieView>> = TODO()
    val upcoming: Flow<LoadableList<MovieView>> = TODO()

}