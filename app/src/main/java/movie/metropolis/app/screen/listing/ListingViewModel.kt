package movie.metropolis.app.screen.listing

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.settings.SettingsFacade
import movie.metropolis.app.util.retainStateIn
import movie.metropolis.app.util.throttleWithTimeout
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@Stable
@HiltViewModel
class ListingViewModel @Inject constructor(
    handle: SavedStateHandle,
    factory: ListingFacade.Factory,
    private val settings: SettingsFacade
) : ViewModel() {

    private val facade = when (handle.contains("type")) {
        true -> factory.upcoming()
        else -> factory.current()
    }

    private val items = facade.get()
        .throttleWithTimeout(100.milliseconds)
        .catch { it.printStackTrace() }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    val promotions = items
        .map { it.promotions.toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())

    val movies = items
        .map { it.items.toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())

    fun favorite(view: MovieView) {
        viewModelScope.launch {
            facade.toggle(view)
        }
    }

    fun hide(view: MovieView) {
        settings.filters += view.name.substringBefore(":")
    }

}