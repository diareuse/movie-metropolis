package movie.core.model

interface MovieReference : Movie {
    val posterUrl: String
    val videoUrl: String?
}