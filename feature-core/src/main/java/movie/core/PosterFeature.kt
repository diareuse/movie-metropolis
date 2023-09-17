package movie.core

interface PosterFeature {

    suspend fun get(): List<String>

}