package movie.rating

class CorrelationWords : Correlation {

    override fun getFactor(first: String, second: String): Float {
        val myWords = first.split(" ")
            .filter { it.isNotBlank() }.map { it.lowercase() }
        val otherWords = second.split(" ")
            .filter { it.isNotBlank() }.map { it.lowercase() }
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

    private fun average(vararg values: Float): Float {
        return values.sum() / values.size
    }

}