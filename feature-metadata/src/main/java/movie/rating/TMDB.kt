package movie.rating

internal object TMDB {

    private const val Url = "https://api.themoviedb.org/3"
    private const val ImageUrl = "https://image.tmdb.org/t/p/w1280"

    fun url(fragments: String) = "$Url$fragments"
    fun image(fragments: String) = "$ImageUrl$fragments"

}