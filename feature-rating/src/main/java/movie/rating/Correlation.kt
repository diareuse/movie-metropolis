package movie.rating

interface Correlation {

    fun getFactor(first: String, second: String): Float

    companion object {

        fun create(): Correlation {
            var out: Correlation
            out = CorrelationAverage(
                CorrelationWords(),
                CorrelationCharacters()
            )
            out = CorrelationLowercase(out)
            out = CorrelationNormalize(out)
            out = CorrelationRemoveArticles(out)
            return out
        }

        internal infix fun String.correlate(other: String): Float {
            if (trim() == other.trim()) return 1f
            return create().getFactor(this, other)
        }

    }

}