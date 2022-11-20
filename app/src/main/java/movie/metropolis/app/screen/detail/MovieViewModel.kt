package movie.metropolis.app.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val detail: Flow<Loadable<MovieDetailView>> = TODO()
    val trailer: Flow<Loadable<VideoView>> = TODO()
    val poster: Flow<Loadable<ImageView>> = TODO()

}