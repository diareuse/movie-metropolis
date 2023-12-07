package movie.metropolis.app.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

fun <T> TestScope.testFlow(
    producer: Flow<T>
): List<T> {
    val output = mutableListOf<T>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        producer.toCollection(output)
    }
    return output
}