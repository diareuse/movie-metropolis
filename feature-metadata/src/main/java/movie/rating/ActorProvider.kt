package movie.rating

interface ActorProvider {

    suspend fun get(query: String): Actor

}