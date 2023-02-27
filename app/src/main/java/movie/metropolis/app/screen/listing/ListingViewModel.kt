package movie.metropolis.app.screen.listing

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.actionsFlow
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.groupFlow
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.promotionsFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class ListingViewModel @Inject constructor(
    factory: ListingFacade.Factory
) : ViewModel() {

    private val upcomingFacade = factory.upcoming()

    private val current = factory.current().actionsFlow
        .shareIn(viewModelScope, SharingStarted.Lazily, 1)
    private val upcoming = upcomingFacade.actionsFlow
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
        upcomingFacade.toggle(movie)
    }

}