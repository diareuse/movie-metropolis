package movie.metropolis.app.feature.global.model

interface MovieReference : Movie {
    val posterUrl: String
    val videoUrl: String?
}