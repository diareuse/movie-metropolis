package movie.rating

interface LinkProvider {

    suspend fun getLink(descriptor: MovieDescriptor): String

}

