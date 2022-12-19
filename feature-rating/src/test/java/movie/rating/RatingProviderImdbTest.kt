package movie.rating

import io.ktor.client.request.HttpRequestData
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RatingProviderImdbTest : AbstractRatingTest() {

    override fun prepare() {}

    override fun respond(request: HttpRequestData): String {
        if (!request.url.host.contains("imdb"))
            throw IllegalArgumentException()
        return when {
            request.url.toString().contains("/title/") -> "imdb-detail.html"
            else -> "imdb-query.html"
        }
    }

    @Test
    fun returns_imdb() = runTest {
        val result = provider.getRating(descriptor)
        assertEquals(81, result)
    }

}