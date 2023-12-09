package movie.rating

internal object TMDB {

    private const val Url = "https://www.themoviedb.org"
    private const val Api = "https://api.themoviedb.org/3"
    private const val ImageUrl = "https://image.tmdb.org/t/p/w1280"

    fun api(fragments: String) = "$Api$fragments"
    fun image(fragments: String) = "$ImageUrl$fragments"
    fun url(fragments: String) = "$Url$fragments"

}