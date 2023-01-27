package movie.metropolis.app.screen.listing

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.currentFlow
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.upcomingFlow
import movie.metropolis.app.presentation.mapLoadable
import movie.metropolis.app.util.retainStateIn
import movie.style.state.ImmutableList.Companion.immutable
import javax.inject.Inject

@Stable
@HiltViewModel
class ListingViewModel @Inject constructor(
    private val facade: ListingFacade
) : ViewModel() {

    val current = facade.currentFlow
        .mapLoadable { it.immutable() }
        .retainStateIn(viewModelScope, Loadable.loading())
    val upcoming = facade.upcomingFlow
        .mapLoadable { it.immutable() }
        .retainStateIn(viewModelScope, Loadable.loading())

    fun toggleFavorite(movie: MovieView) = viewModelScope.launch {
        facade.toggleFavorite(movie)
    }

}