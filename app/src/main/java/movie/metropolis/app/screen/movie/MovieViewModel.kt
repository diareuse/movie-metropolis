package movie.metropolis.app.screen.movie

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.presentation.detail.MovieFacade
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class MovieViewModel private constructor(
    id: String,
    facade: MovieFacade,
    val hideShowings: Boolean
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
        facade.create(args.movie),
        args.upcoming
    )

    val movie = facade.movie
        .retainStateIn(viewModelScope, MovieDetailView(id))

}