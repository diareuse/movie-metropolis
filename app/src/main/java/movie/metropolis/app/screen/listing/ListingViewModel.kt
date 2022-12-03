package movie.metropolis.app.screen.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.listing.ListingFacade.Companion.currentFlow
import movie.metropolis.app.screen.listing.ListingFacade.Companion.upcomingFlow
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    facade: ListingFacade
) : ViewModel() {

    val current = facade.currentFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val upcoming = facade.upcomingFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

}