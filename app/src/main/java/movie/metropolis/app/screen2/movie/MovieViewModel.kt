package movie.metropolis.app.screen2.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapNotNull
import movie.core.UserDataFeature
import movie.metropolis.app.presentation.detail.MovieFacade
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class MovieViewModel private constructor(
    private val facade: MovieFacade.Filterable,
    private val user: UserDataFeature,
    val hideShowings: Boolean
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        facade: MovieFacade.Factory,
        user: UserDataFeature
    ) : this(
        Route.Movie.Arguments(handle),
        facade,
        user
    )

    constructor(
        args: Route.Movie.Arguments,
        facade: MovieFacade.Factory,
        user: UserDataFeature
    ) : this(
        facade.create(args.movie),
        user,
        args.upcoming
    )

    val movie = facade.movie.mapNotNull { it.getOrNull() }
        .retainStateIn(viewModelScope, null)

}