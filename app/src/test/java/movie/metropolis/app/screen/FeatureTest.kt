package movie.metropolis.app.screen

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import movie.core.EventFeature
import movie.core.UserFeature
import org.junit.Before
import org.mockito.kotlin.mock

abstract class FeatureTest {

    //protected lateinit var account: UserAccount
    //protected lateinit var credentials: UserCredentials
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
        //val network = movie.core.nwk.di.NetworkModule()
        //val root = network.clientRoot(engine)
        prepareEvent(/*network.clientData(root), root*/)
        prepareUser(/*network.clientCustomer(root)*/)
        prepare()
    }

    private fun prepareEvent(/*clientData: HttpClient, clientRoot: HttpClient*/) {
        //val module = EventFeatureModule()
        event = mock()//module.feature(module.event(clientData), module.cinema(clientRoot))
    }

    private fun prepareUser(/*client: HttpClient*/) {
        //account = spy(MockAccount())
        //credentials = spy(MockCredentials())
        //val module = UserFeatureModule()
        //val auth = UserFeatureModule.AuthMetadata("user", "password", "captcha")
        //val service = module.service(client, account, credentials, auth)
        user = mock()//module.feature(service, account, event)
    }

    /*private open class MockAccount : movie.core.auth.UserAccount {
        override val isLoggedIn: Boolean
            get() = token != null
        override var token: String? = null
        override var refreshToken: String? = null
        override var expirationDate: Date? = null
        override fun delete() {}
    }

    private open class MockCredentials : movie.core.auth.UserCredentials {
        override var email: String? = "test@google.com"
        override var password: String? = "foobartoolbar"
    }*/

}