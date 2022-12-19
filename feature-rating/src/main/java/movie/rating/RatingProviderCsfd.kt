package movie.rating

import io.ktor.client.HttpClient

class RatingProviderCsfd(
    private val client: HttpClient
) : RatingProvider {

    override suspend fun getRating(descriptor: MovieDescriptor): Byte {
        TODO("Not yet implemented")
    }

}