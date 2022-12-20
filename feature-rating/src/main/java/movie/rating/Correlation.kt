package movie.rating

import java.text.Normalizer.Form
import java.text.Normalizer.isNormalized
import java.text.Normalizer.normalize
import kotlin.math.max
import kotlin.math.min

internal infix fun String.correlate(other: String): Float {
    if (trim() == other.trim()) return 1f
    if (
        !isNormalized(this, Form.NFKD) ||
        !isNormalized(other, Form.NFKD)
    ) {
        return normalize(this, Form.NFKD) correlate normalize(other, Form.NFKD)
    }

    fun charFactor(): Float {
        val mySize = toSet().size
        val otherSize = other.toSet().size
        return min(mySize, otherSize).toFloat() / max(mySize, otherSize)
    }

    fun wordFactor(): Float {
        val myWords = split(" ").map { it.lowercase() }
        val otherWords = other.split(" ").map { it.lowercase() }
        val words = buildSet {
            addAll(myWords)
            addAll(otherWords)
        }
        val myHits = myWords.count { it in words }
        val otherHits = otherWords.count { it in words }
        return average(
            myHits.toFloat() / words.size,
            otherHits.toFloat() / words.size
        )
    }

    return average(
        charFactor(),
        wordFactor()
    )
}

private fun average(vararg values: Float): Float {
    return values.sum() / values.size
}