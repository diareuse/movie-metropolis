package movie.metropolis.app.screen.setup

import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable

interface SetupFacade {

    val requiresSetup: Boolean

    suspend fun getRegions(): Result<List<RegionView>>
    suspend fun select(view: RegionView)

    companion object {

        val SetupFacade.regionsFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getRegions().asLoadable())
            }

    }

}