package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.Filter
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.OnChangedListener
import movie.metropolis.app.screen.asLoadable
import kotlin.time.Duration.Companion.seconds

interface BookingFilterable {

    suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>>

    fun toggle(filter: Filter)
    fun addOnChangedListener(listener: OnChangedListener): OnChangedListener
    fun removeOnChangedListener(listener: OnChangedListener)

    companion object {

        val BookingFilterable.optionsChangedFlow
            get() = callbackFlow {
                val listener = addOnChangedListener {
                    trySend(Any())
                }
                send(Any())
                awaitClose {
                    removeOnChangedListener(listener)
                }
            }.debounce(1.seconds)

        val BookingFilterable.optionsFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getOptions().asLoadable())
                optionsChangedFlow.collect {
                    emit(getOptions().asLoadable())
                }
            }.debounce(1.seconds)

    }

}