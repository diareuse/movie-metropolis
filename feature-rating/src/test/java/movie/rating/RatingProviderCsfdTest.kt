package movie.rating

import io.ktor.client.request.HttpRequestData
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RatingProviderCsfdTest : AbstractRatingTest() {

    override val domain: String
        get() = "csfd.cz"

    override fun prepare() {
        descriptor = MovieDescriptor.Local("Avatar: The Way of Water", 2022)
    }

    override fun respond(request: HttpRequestData): String {
        if (!request.url.host.contains("csfd"))
            throw IllegalArgumentException()
        return when {
            request.url.toString().contains("/film/") -> "csfd-detail.html"
            else -> "csfd-query.html"
        }
    }

    @Test
    fun returns_fromNetwork() = runTest {
        val result = provider(this).get(descriptor).csfd?.value
        assertEquals(86, result)
    }

    @Test
    fun returns_fromDatabase() = runTest {
        val rating = database_returns_success()
        val result = provider(this).get(descriptor).csfd?.value
        assertEquals(rating, result)
    }

}