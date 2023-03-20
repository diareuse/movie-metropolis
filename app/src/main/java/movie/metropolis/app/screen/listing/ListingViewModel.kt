package movie.metropolis.app.screen.listing

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.groups
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.promotions
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class ListingViewModel @Inject constructor(
    factory: ListingFacade.Factory
) : ViewModel() {

    private val upcomingFacade = factory.upcoming()
    private val currentFacade = factory.current()

    val currentPromotions = currentFacade.promotions
        .retainStateIn(viewModelScope)
    val upcomingPromotions = upcomingFacade.promotions
        .retainStateIn(viewModelScope)

    val currentGroups = currentFacade.groups
        .retainStateIn(viewModelScope)
    val upcomingGroups = upcomingFacade.groups
        .retainStateIn(viewModelScope)

    fun toggleFavorite(movie: MovieView) = viewModelScope.launch {
        upcomingFacade.toggle(movie)
    }

}