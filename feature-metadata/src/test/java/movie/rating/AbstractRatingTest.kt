package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job
import kotlinx.coroutines.test.runTest
import movie.log.Logger
import movie.log.PlatformLogger
import movie.rating.database.RatingDao
import movie.rating.database.RatingStored
import movie.rating.di.RatingProviderModule
import movie.rating.internal.LazyHttpClient
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.random.Random.Default.nextInt

abstract class AbstractRatingTest {

    internal lateinit var dao: RatingDao
    protected lateinit var descriptor: MovieDescriptor
    protected lateinit var provider: (CoroutineScope) -> MetadataProvider.Composed

    @Before
    fun prepareInternal() {
        Logger.setLogger(PlatformLogger())
        val config = MockEngineConfig().apply {
            addHandler {
                try {
                    respondOk(respond(it).let(::resourceFile))
                } catch (e: Throwable) {
                    respondBadRequest()
                }
            }
        }
        val client = LazyHttpClient { HttpClient(MockEngine(config)) {} }
        val module = RatingProviderModule()
        dao = mock()
        provider = {
            module.rating(
                tomatoes = module.rtRating(client, dao, it),
                imdb = module.imdbRating(client, dao, it),
                csfd = module.csfdRating(client, dao, it)
            )
        }
        descriptor = MovieDescriptor.Original("Avatar: The Way of Water", 2022)
        prepare()
    }

    abstract fun prepare()
    abstract fun respond(request: HttpRequestData): String
    abstract val domain: String

    private fun resourceFile(name: String) = Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(name)
        ?.use { it.readBytes() }
        ?.let(::String).orEmpty()

    internal suspend fun database_returns_success(): Byte {
        val data = RatingStored(descriptor.name, descriptor.year, nextInt(0, 100).toByte(), "url")
        whenever(dao.select(any(), any(), eq("%$domain%"))).thenReturn(data)
        return data.rating
    }

    @Test
    fun stores_csfd() = runTest {
        provider(this).get(descriptor)
        currentCoroutineContext().job.children.forEach { it.join() }
        verify(dao, times(1)).insertOrUpdate(any())
    }

}