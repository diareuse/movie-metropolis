package movie.metropolis.app.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.screen.retainStateIn
import movie.metropolis.app.screen.settings.SettingsFacade.Companion.filterSeenFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val facade: SettingsFacade
) : ViewModel() {

    val filterSeen = facade.filterSeenFlow
        .retainStateIn(viewModelScope, false)

    fun updateFilterSeen(value: Boolean) {
        facade.filterSeen = value
    }

}