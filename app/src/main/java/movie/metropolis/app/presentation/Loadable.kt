package movie.metropolis.app.presentation

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// do not convert to value class, it's being marked as unstable by compose compiler
@Immutable
class Loadable<out Result>(private val value: Any?) {

    val isLoading get() = value is Loading
    val isFailure get() = value is Failure
    val isSuccess get() = !isLoading && !isFailure

    fun getOrNull() = when (value) {
        is Loading -> null
        is Failure -> null
        else -> value as Result
    }

    fun exceptionOrNull() = when (value) {
        is Failure -> value.throwable
        else -> null
    }

    private object Loading {
        override fun toString(): String {
            return "Loading"
        }
    }

    private class Failure(val throwable: Throwable) {
        override fun toString(): String {
            return "Failure(throwable=$throwable)".also { throwable.printStackTrace() }
        }
    }

    companion object {

        fun <T> loading(): Loadable<T> = Loadable(Loading)
        fun <T> failure(throwable: Throwable): Loadable<T> = Loadable(Failure(throwable))
        fun <T> success(value: T): Loadable<T> = Loadable(value)

    }

}

fun <T> Result<T>.asLoadable() = fold(
    onSuccess = { Loadable.success(it) },
    onFailure = { Loadable.failure(it) })

fun <T> Loadable<T>.getOrThrow(): T {
    throw exceptionOrNull() ?: return getOrNull() as T
}

inline fun <T> Loadable<List<T>>.onEmpty(body: () -> Unit) = onSuccess {
    if (it.isEmpty()) body()
}

inline fun <T> Loadable<T>.onSuccess(body: (T) -> Unit) = apply {
    if (isSuccess) body(getOrNull() as T)
}

inline fun <T> Loadable<T>.onLoading(body: () -> Unit) = apply {
    if (isLoading) body()
}

inline fun <T> Loadable<T>.onFailure(body: (Throwable) -> Unit) = apply {
    if (isFailure) body(checkNotNull(exceptionOrNull()))
}

inline fun <T, R> Loadable<T>.map(mapper: (T) -> R) = when {
    isSuccess -> Loadable.success(mapper(getOrThrow()))
    else -> this as Loadable<R>
}

inline fun <T, R : Any> Loadable<T>.mapNotNull(mapper: (T) -> R?) = when {
    isSuccess -> when (val value = mapper(getOrThrow())) {
        null -> Loadable.failure(NullPointerException("Mapped value is null"))
        else -> Loadable.success(value)
    }

    else -> this as Loadable<R>
}

inline fun <T, R> Loadable<T>.fold(
    onSuccess: (T) -> R,
    onLoading: () -> R,
    onFailure: (Throwable) -> R
): R = when {
    isSuccess -> onSuccess(getOrThrow())
    isLoading -> onLoading()
    isFailure -> onFailure(exceptionOrNull() ?: IllegalStateException())
    else -> throw IllegalStateException()
}

fun <T, R> Flow<Loadable<T>>.mapLoadable(body: (T) -> R) =
    map { it.map(body) }