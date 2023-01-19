package movie.core.util

import kotlinx.coroutines.runBlocking
import org.mockito.invocation.InvocationOnMock
import org.mockito.kotlin.KStubbing
import org.mockito.kotlin.whenever
import org.mockito.stubbing.OngoingStubbing

inline fun <T> wheneverSus(crossinline body: suspend () -> T) = whenever(runBlocking { body() })
inline fun <T, R> KStubbing<T>.onSus(crossinline body: suspend T.() -> R) =
    on { runBlocking { body() } }

inline fun <T, R> OngoingStubbing<T>.thenAnswerSus(crossinline body: suspend InvocationOnMock.() -> R) =
    thenAnswer { runBlocking { body(it) } }