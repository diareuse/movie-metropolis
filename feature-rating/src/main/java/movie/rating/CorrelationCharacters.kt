package movie.rating

import kotlin.math.max
import kotlin.math.min

class CorrelationCharacters : Correlation {

    override fun getFactor(first: String, second: String): Float {
        val mySize = first.toSet().size
        val otherSize = second.toSet().size
        return min(mySize, otherSize).toFloat() / max(mySize, otherSize)
    }

}