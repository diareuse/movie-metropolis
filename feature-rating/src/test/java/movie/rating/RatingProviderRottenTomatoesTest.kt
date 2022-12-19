package movie.rating

import io.ktor.client.request.HttpRequestData
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RatingProviderRottenTomatoesTest : AbstractRatingTest() {

    override fun prepare() {}

    override fun respond(request: HttpRequestData): String {
        if (!request.url.host.contains("rottentomatoes"))
            throw IllegalArgumentException()
        return when {
            request.url.toString().contains("/m/") -> "rt-detail.html"
            else -> "rt-query.html"
        }
    }

    @Test
    fun returns_rottenTomatoes() = runTest {
        val result = provider.getRating(descriptor)
        assertEquals(94, result)
    }

}