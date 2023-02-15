package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestData
import movie.log.Logger
import movie.log.PlatformLogger
import movie.rating.di.RatingProviderModule
import org.junit.Before

abstract class AbstractRatingTest {

    protected lateinit var descriptor: MovieDescriptor
    protected lateinit var provider: RatingProvider.Composed

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
        val client = HttpClient(MockEngine(config)) {}
        val module = RatingProviderModule()
        provider = module.rating(
            tomatoes = module.rtRating(client),
            imdb = module.imdbRating(client),
            csfd = module.csfdRating(client)
        )
        descriptor = MovieDescriptor.Original("Avatar: The Way of Water", 2022)
        prepare()
    }

    abstract fun prepare()
    abstract fun respond(request: HttpRequestData): String

    private fun resourceFile(name: String) = Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(name)
        ?.use { it.readBytes() }
        ?.let(::String).orEmpty()

}