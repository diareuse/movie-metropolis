package movie.rating

import io.ktor.client.request.HttpRequestData
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RatingProviderCsfdTest : AbstractRatingTest() {

    override fun prepare() {}

    override fun respond(request: HttpRequestData): String {
        if (!request.url.host.contains("csfd"))
            throw IllegalArgumentException()
        return when {
            request.url.toString().contains("/film/") -> "csfd-detail.html"
            else -> "csfd-query.html"
        }
    }

    @Test
    fun returns_csfd() = runTest {
        val result = provider.get(descriptor).csfd?.value
        assertEquals(86, result)
    }

}