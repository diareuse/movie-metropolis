package movie.rating

import java.text.Normalizer

class CorrelationNormalize(
    private val origin: Correlation
) : Correlation {

    override fun getFactor(first: String, second: String): Float {
        return origin.getFactor(
            Normalizer.normalize(first, Normalizer.Form.NFKD),
            Normalizer.normalize(second, Normalizer.Form.NFKD)
        )
    }

}