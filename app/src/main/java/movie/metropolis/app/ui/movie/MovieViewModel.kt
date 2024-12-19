package movie.metropolis.app.ui.movie

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import movie.metropolis.app.presentation.detail.MovieFacade
import movie.metropolis.app.screen.Route
import javax.inject.Inject

@Stable
@HiltViewModel
class MovieViewModel private constructor(
    id: String,
    facade: MovieFacade
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        facade: MovieFacade.Factory
    ) : this(
        Route.Movie.Arguments(handle),
        facade
    )

    constructor(
        args: Route.Movie.Arguments,
        facade: MovieFacade.Factory,
    ) : this(
        args.movie,
        facade.create(args.movie)
    )

    val state = MovieDetailState()

    init {
        facade.movie
            .onEach { state.detail = it }
            .launchIn(viewModelScope)
    }

}