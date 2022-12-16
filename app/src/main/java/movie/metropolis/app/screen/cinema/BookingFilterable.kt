package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.Filter
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable

interface BookingFilterable {

    suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>>

    fun toggle(filter: Filter)
    fun addOnChangedListener(listener: OnChangedListener): OnChangedListener
    fun removeOnChangedListener(listener: OnChangedListener)

    fun interface OnChangedListener {
        fun onChanged()
    }

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
            }

        val BookingFilterable.optionsFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getOptions().asLoadable())
                optionsChangedFlow.collect {
                    emit(getOptions().asLoadable())
                }
            }

    }

}