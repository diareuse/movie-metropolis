package movie.core.util

import kotlinx.coroutines.runBlocking
import movie.core.ResultCallback
import org.mockito.invocation.InvocationOnMock
import org.mockito.kotlin.whenever
import org.mockito.stubbing.OngoingStubbing

inline fun <T> wheneverBlocking(crossinline body: suspend () -> T) =
    whenever(runBlocking { body() })

inline fun <T, R> OngoingStubbing<T>.thenBlocking(crossinline body: suspend InvocationOnMock.() -> R) =
    thenAnswer { runBlocking { body(it) } }

fun <T> InvocationOnMock.callback(index: Int, body: suspend () -> Result<T>) = runBlocking {
    getArgument<ResultCallback<T>>(index).invoke(body())
}