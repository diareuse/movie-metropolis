package movie.style.state

import androidx.compose.runtime.Immutable
import java.util.Collections

@Immutable
class ImmutableSet<T>(list: Set<T>) : Set<T> by Collections.unmodifiableSet(list) {

    companion object {

        fun <T> Set<T>.immutable() = when (this) {
            is ImmutableSet<T> -> this
            else -> ImmutableSet(this)
        }

    }

}