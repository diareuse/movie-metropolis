package movie.rating

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job
import kotlinx.coroutines.test.runTest
import movie.log.Logger
import movie.log.PlatformLogger
import movie.rating.database.RatingDao
import movie.rating.database.RatingStored
import movie.rating.di.RatingProviderModule
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.random.Random.Default.nextInt

abstract class AbstractRatingTest {

    private lateinit var dao: RatingDao
    protected lateinit var descriptor: MovieDescriptor
    protected lateinit var provider: (CoroutineScope) -> MetadataProvider

    @Before
    fun prepareInternal() {
        Logger.setLogger(PlatformLogger())
        val config = MockEngineConfig().apply {
            addHandler {
                try {
                    respond(
                        respond(it).let(::resourceFile),
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                } catch (e: Throwable) {
                    respondBadRequest()
                }
            }
        }
        val client = RatingProviderModule().client(MockEngine(config))
        val module = RatingProviderModule()
        dao = mock()
        provider = {
            module.rating(client, dao)
        }
        descriptor = MovieDescriptor.Original("Avatar: The Way of Water", 2022)
        prepare()
    }

    abstract fun prepare()
    abstract fun respond(request: HttpRequestData): String

    private fun resourceFile(name: String) = Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(name)
        ?.use { it.readBytes() }
        ?.let(::String).orEmpty()

    internal suspend fun database_returns_success(): Byte {
        val data = RatingStored(
            name = descriptor.name,
            year = descriptor.year,
            rating = nextInt(0, 100).toByte(),
            poster = "url",
            overlay = "url"
        )
        whenever(dao.select(any(), any())).thenReturn(data)
        return data.rating
    }

    @Test
    fun stores_csfd() = runTest {
        provider(this).get(descriptor)
        currentCoroutineContext().job.children.forEach { it.join() }
        verify(dao, times(1)).insertOrUpdate(any())
    }

}