package movie.metropolis.app.screen

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import movie.metropolis.app.di.NetworkModule
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.di.EventFeatureModule
import movie.metropolis.app.feature.user.UserAccount
import movie.metropolis.app.feature.user.UserCredentials
import movie.metropolis.app.feature.user.UserFeature
import movie.metropolis.app.feature.user.di.UserFeatureModule
import org.junit.Before
import org.mockito.kotlin.spy
import java.util.Date

abstract class ViewModelTest {

    protected lateinit var account: UserAccount
    protected lateinit var credentials: UserCredentials
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
        val engine = MockEngine(config)
        val network = NetworkModule()
        prepareEvent(network.clientData(engine), network.clientRoot(engine))
        prepareUser(network.clientCustomer(engine))
        prepare()
    }

    private fun prepareEvent(clientData: HttpClient, clientRoot: HttpClient) {
        event = EventFeatureModule().feature(clientData)
    }

    private fun prepareUser(client: HttpClient) {
        account = spy(MockAccount())
        credentials = spy(MockCredentials())
        val module = UserFeatureModule()
        val service = module.service(client, account, credentials)
        user = module.feature(service, account)
    }

    private open class MockAccount : UserAccount {
        override val isLoggedIn: Boolean
            get() = token != null
        override var token: String? = null
        override var refreshToken: String? = null
        override var expirationDate: Date? = null
    }

    private open class MockCredentials : UserCredentials {
        override var email: String? = "test@google.com"
        override var password: String? = "foobartoolbar"
    }

}