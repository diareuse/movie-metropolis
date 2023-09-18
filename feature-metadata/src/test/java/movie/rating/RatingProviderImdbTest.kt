package movie.rating

import io.ktor.client.request.HttpRequestData
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RatingProviderImdbTest : AbstractRatingTest() {

    override val domain: String
        get() = "imdb.com"

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
    fun returns_fromNetwork() = runTest {
        val result = provider(this).get(descriptor).imdb?.value
        assertEquals(81, result)
    }

    @Test
    fun returns_fromDatabase() = runTest {
        val rating = database_returns_success()
        val result = provider(this).get(descriptor).imdb?.value
        assertEquals(rating, result)
    }

}