package movie.rating

class CorrelationRemoveArticles(
    private val origin: Correlation
) : Correlation {

    override fun getFactor(first: String, second: String): Float {
        return origin.getFactor(first.removeArticles(), second.removeArticles())
    }

    private fun String.removeArticles() = this
        .replace("the", "", true)
        .replace(" a ", " ", true)

}