package movie.metropolis.app.screen.setup

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.setup.SetupFacade
import movie.metropolis.app.presentation.setup.SetupFacade.Companion.regionsFlow
import movie.metropolis.app.presentation.setup.SetupFacade.Companion.requiresSetupFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
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