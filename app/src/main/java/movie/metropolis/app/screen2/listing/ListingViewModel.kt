package movie.metropolis.app.screen2.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.groups
import movie.metropolis.app.presentation.listing.ListingFacade.Companion.promotions
import movie.metropolis.app.presentation.mapLoadable
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    factory: ListingFacade.Factory
) : ViewModel() {

    private val facade = factory.current()

    val promotions = facade.promotions
        .map { it.getOrNull().orEmpty() }
        .retainStateIn(viewModelScope, emptyList())

    val movies = facade.groups
        .mapLoadable { it.values.flatten().distinctBy { it.id } }
        .map { it.getOrNull().orEmpty() }
        .retainStateIn(viewModelScope, emptyList())

    fun favorite(view: MovieView) {
        viewModelScope.launch {
            facade.toggle(view)
        }
    }

}