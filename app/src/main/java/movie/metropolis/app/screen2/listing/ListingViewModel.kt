package movie.metropolis.app.screen2.listing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
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
    handle: SavedStateHandle,
    factory: ListingFacade.Factory
) : ViewModel() {

    private val facade = when (handle.contains("type")) {
        true -> factory.upcoming()
        else -> factory.current()
    }

    val promotions = facade.promotions
        .map { it.getOrNull().orEmpty().toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())

    val movies = facade.groups
        .mapLoadable { it.values.flatten().distinctBy { it.id } }
        .map { it.getOrNull().orEmpty().toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())

    fun favorite(view: MovieView) {
        viewModelScope.launch {
            facade.toggle(view)
        }
    }

}