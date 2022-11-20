package movie.metropolis.app.screen

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import movie.metropolis.app.di.NetworkModule
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.di.EventFeatureModule
import movie.metropolis.app.feature.user.UserFeature
import movie.metropolis.app.feature.user.di.UserFeatureModule
import org.junit.Before

abstract class ViewModelTest {

    protected lateinit var responder: UrlResponder
    protected lateinit var user: UserFeature
    protected lateinit var event: EventFeature
    protected lateinit var config: MockEngineConfig

    abstract fun prepare()

    @Before
    fun prepareInternal() {
        config = MockEngineConfig()
        responder = UrlResponder()
        config.addHandler(responder)
        val client = NetworkModule().client(MockEngine(config))
        event = EventFeatureModule().feature(client)
        user = UserFeatureModule().feature(client)
        prepare()
    }

    protected fun file(name: String) = Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(name)
        ?.use { it.readBytes() }
        ?.let(::String).orEmpty()

}