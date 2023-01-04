package movie.rating

class CorrelationAverage(
    private vararg val factors: Correlation
) : Correlation {

    override fun getFactor(first: String, second: String): Float {
        return factors.map { it.getFactor(first, second) }.average().toFloat()
    }

}