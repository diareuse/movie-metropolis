package movie.metropolis.app.util

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration

fun <T> Flow<T>.throttleWithTimeout(timeout: Duration) = channelFlow {
    var emissionJob: Job? = null
    collect {
        val throttle = emissionJob != null
        emissionJob?.cancel()
        emissionJob = launch {
            if (throttle) delay(timeout)
            send(it)
        }
    }
}