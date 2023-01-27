package movie.style.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.util.Collections

@Immutable
@Stable
class ImmutableList<T>(list: List<T>) : List<T> by Collections.unmodifiableList(list) {

    companion object {

        fun <T> List<T>.immutable() = when (this) {
            is ImmutableList<T> -> this
            else -> ImmutableList(this)
        }

    }

}