package movie.metropolis.app.presentation.setup

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.asLoadable
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
            get() = channelFlow {
                while (currentCoroutineContext().isActive) {
                    withContext(Dispatchers.IO) {
                        send(requiresSetup)
                    }
                    delay(1.seconds)
                }
            }.distinctUntilChanged()

    }

}