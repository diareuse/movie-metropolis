package movie.core.model

sealed class Region {

    abstract val domain: String
    internal abstract val id: Int

    object Czechia : Region() {
        override val domain: String
            get() = "https://www.cinemacity.cz"
        override val id: Int
            get() = 10101
    }

    object Slovakia : Region() {
        override val domain: String
            get() = "https://www.cinemacity.sk"
        override val id: Int
            get() = 10105
    }

    object Hungary : Region() {
        override val domain: String
            get() = "https://www.cinemacity.hu"
        override val id: Int
            get() = 10102
    }

    object Romania : Region() {
        override val domain: String
            get() = "https://www.cinemacity.ro"
        override val id: Int
            get() = 10107
    }

    object Poland : Region() {
        override val domain: String
            get() = "https://www.cinemacity.pl"
        override val id: Int
            get() = 10103
    }

    class Custom(
        override val domain: String,
        override val id: Int
    ) : Region()

    companion object {

        fun by(domain: String, id: Int) = when (domain) {
            Czechia.domain -> Czechia
            Slovakia.domain -> Slovakia
            Hungary.domain -> Hungary
            Romania.domain -> Romania
            Poland.domain -> Poland
            else -> Custom(domain, id)
        }

    }

}