package movie.metropolis.app.screen.setup

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import kotlin.time.Duration.Companion.seconds

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

        val SetupFacade.requiresSetupFlow
            get() = flow {
                while (currentCoroutineContext().isActive) {
                    emit(requiresSetup)
                    delay(1.seconds)
                }
            }

    }

}