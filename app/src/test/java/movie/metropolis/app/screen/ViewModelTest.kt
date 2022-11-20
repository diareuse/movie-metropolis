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

    private lateinit var user: UserFeature
    private lateinit var event: EventFeature
    private lateinit var config: MockEngineConfig

    abstract fun prepare()

    @Before
    fun prepareInternal() {
        config = MockEngineConfig()
        val client = NetworkModule().client(MockEngine(config))
        event = EventFeatureModule().feature(client)
        user = UserFeatureModule().feature(client)
        prepare()
    }

}