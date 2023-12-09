package movie.metropolis.app.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.favorite.FavoriteFacade
import movie.metropolis.app.presentation.settings.SettingsFacade
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val facade: FavoriteFacade,
    private val settings: SettingsFacade
) : ViewModel() {

    val items = facade.get()
        .map { it.toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())

    fun remove(view: MovieView) {
        viewModelScope.launch {
            facade.remove(view)
        }
    }

    fun hide(view: MovieView) {
        settings.filters += view.name.substringBefore(":")
    }

}