package movie.style.state

import androidx.compose.runtime.*
import java.util.Collections

@Immutable
class ImmutableMap<K, V>(map: Map<K, V>) : Map<K, V> by Collections.unmodifiableMap(map) {

    companion object {

        fun <K, V> Map<K, V>.immutable() = when (this) {
            is ImmutableMap<K, V> -> this
            else -> ImmutableMap(this)
        }

        fun <K, V> immutableMapOf(vararg pairs: Pair<K, V>) = mapOf(pairs = pairs).immutable()

    }

}