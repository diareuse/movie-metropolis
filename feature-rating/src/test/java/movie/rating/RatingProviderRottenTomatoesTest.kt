package movie.rating

import io.ktor.client.request.HttpRequestData
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RatingProviderRottenTomatoesTest : AbstractRatingTest() {

    override val domain: String
        get() = "rottentomatoes.com"

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
    fun returns_fromNetwork() = runTest {
        val result = provider(this).get(descriptor).rottenTomatoes?.value
        assertEquals(94, result)
    }

    @Test
    fun returns_fromDatabase() = runTest {
        val rating = database_returns_success()
        val result = provider(this).get(descriptor).rottenTomatoes?.value
        assertEquals(rating, result)
    }

}