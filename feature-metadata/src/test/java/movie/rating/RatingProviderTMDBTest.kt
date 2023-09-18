package movie.rating

import io.ktor.client.request.HttpRequestData
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RatingProviderTMDBTest : AbstractRatingTest() {

    override fun prepare() {
        descriptor = MovieDescriptor.Local("Avatar: The Way of Water", 2022)
    }

    override fun respond(request: HttpRequestData): String {
        return "tmdb-response.json"
    }

    @Test
    fun returns_fromNetwork() = runTest {
        val result = provider(this).get(descriptor)?.rating
        assertEquals(76, result)
    }

    @Test
    fun returns_fromDatabase() = runTest {
        val rating = database_returns_success()
        val result = provider(this).get(descriptor)?.rating
        assertEquals(rating, result)
    }

}