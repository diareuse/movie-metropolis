package movie.metropolis.app.util

import kotlinx.coroutines.runBlocking
import org.mockito.invocation.InvocationOnMock
import org.mockito.kotlin.whenever
import org.mockito.stubbing.OngoingStubbing

inline fun <T> wheneverBlocking(crossinline body: suspend () -> T) =
    whenever(runBlocking { body() })

inline fun <T, R> OngoingStubbing<T>.thenBlocking(crossinline body: suspend InvocationOnMock.() -> R) =
    thenAnswer { runBlocking { body(it) } }
