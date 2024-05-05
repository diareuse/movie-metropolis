package movie.cinema.city

interface EndpointProvider {
    val domain: String
    val id: Int
    val tld: String
        get() = domain.substringAfterLast('.').substringBefore('/')
}