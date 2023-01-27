package movie.style.state

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
class ImmutableDate(origin: Date) : Date(origin.time) {

    companion object {

        fun Date.immutable() = when (this) {
            is ImmutableDate -> this
            else -> ImmutableDate(this)
        }

    }

}