package movie.cinema.city

sealed class Region {

    abstract val domain: String
    abstract val id: Int

    data object Czechia : Region() {
        override val domain: String
            get() = "https://www.cinemacity.cz"
        override val id: Int
            get() = 10101
    }

    data object Slovakia : Region() {
        override val domain: String
            get() = "https://www.cinemacity.sk"
        override val id: Int
            get() = 10105
    }

    data object Hungary : Region() {
        override val domain: String
            get() = "https://www.cinemacity.hu"
        override val id: Int
            get() = 10102
    }

    data object Romania : Region() {
        override val domain: String
            get() = "https://www.cinemacity.ro"
        override val id: Int
            get() = 10107
    }

    data object Poland : Region() {
        override val domain: String
            get() = "https://www.cinemacity.pl"
        override val id: Int
            get() = 10103
    }

    companion object {

        fun by(id: Int) = when (id) {
            Czechia.id -> Czechia
            Slovakia.id -> Slovakia
            Hungary.id -> Hungary
            Romania.id -> Romania
            Poland.id -> Poland
            else -> null
        }

    }

}