package movie.metropolis.app.screen.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.listing.ListingAltFacade
import movie.metropolis.app.presentation.listing.ListingAltFacade.Companion.actionsFlow
import movie.metropolis.app.presentation.listing.ListingAltFacade.Companion.groupFlow
import movie.metropolis.app.presentation.listing.ListingAltFacade.Companion.promotionsFlow
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class ListingAltViewModel @Inject constructor(
    factory: ListingAltFacade.Factory,
    private val facade: ListingFacade
) : ViewModel() {

    private val current = factory.current().actionsFlow
        .shareIn(viewModelScope, SharingStarted.Lazily, 1)
    private val upcoming = factory.upcoming().actionsFlow
        .shareIn(viewModelScope, SharingStarted.Lazily, 1)

    val currentPromotions = promotionsFlow(current)
        .retainStateIn(viewModelScope)
    val upcomingPromotions = promotionsFlow(upcoming)
        .retainStateIn(viewModelScope)

    val currentGroups = groupFlow(current)
        .retainStateIn(viewModelScope)
    val upcomingGroups = groupFlow(upcoming)
        .retainStateIn(viewModelScope)

    fun toggleFavorite(movie: MovieView) = viewModelScope.launch {
        facade.toggleFavorite(movie)
    }

}