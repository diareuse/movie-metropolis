package movie.metropolis.app.screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class StateMachine<State>(
    scope: CoroutineScope,
    initial: State,
    private val loading: State.() -> State
) {

    private val channel = Channel<Mutator<State>>()

    val state = channel.consumeAsFlow()
        .filterNotNull()
        .runningLoadingFold(initial) { state, mutator -> mutator.run { state.mutate() } }
        .stateIn(scope, SharingStarted.Eagerly, initial)

    fun submit(mutator: Mutator<State>) {
        channel.trySend(mutator)
    }

    private fun <T> Flow<T>.runningLoadingFold(
        initial: State,
        operation: suspend (accumulator: State, value: T) -> State
    ): Flow<State> = flow {
        var accumulator = initial
        emit(accumulator)
        collect { value ->
            accumulator = loading(accumulator)
            emit(accumulator)
            accumulator = operation(accumulator, value)
            emit(accumulator)
        }
    }

    fun interface Mutator<State> {
        suspend fun State.mutate(): State
    }

}