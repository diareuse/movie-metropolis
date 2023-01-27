package movie.metropolis.app.screen.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.currentFlow
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.upcomingFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val facade: ListingFacade
) : ViewModel() {

    val current = facade.currentFlow
        .retainStateIn(viewModelScope, Loadable.loading())
    val upcoming = facade.upcomingFlow
        .retainStateIn(viewModelScope, Loadable.loading())

    fun toggleFavorite(movie: MovieView) = viewModelScope.launch {
        facade.toggleFavorite(movie)
    }

}