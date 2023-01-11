package movie.metropolis.app.screen.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.retainStateIn
import movie.metropolis.app.screen.setup.SetupFacade.Companion.regionsFlow
import movie.metropolis.app.screen.setup.SetupFacade.Companion.requiresSetupFlow
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val facade: SetupFacade
) : ViewModel() {

    val requiresSetup = facade.requiresSetupFlow
        .retainStateIn(viewModelScope, facade.requiresSetup)

    val regions = facade.regionsFlow
        .retainStateIn(viewModelScope, Loadable.loading())

    fun select(view: RegionView) {
        viewModelScope.launch {
            facade.select(view)
        }
    }

}