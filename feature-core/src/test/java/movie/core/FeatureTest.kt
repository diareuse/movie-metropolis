package movie.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import movie.core.nwk.di.NetworkModule
import movie.log.Logger
import movie.log.PlatformLogger
import org.junit.Before

abstract class FeatureTest {

    protected lateinit var config: MockEngineConfig
    protected lateinit var clientRoot: HttpClient
    protected lateinit var clientData: HttpClient
    protected lateinit var clientCustomer: HttpClient
    protected lateinit var responder: UrlResponder

    abstract fun prepare()

    @Before
    fun prepareInternal() {
        Logger.setLogger(PlatformLogger())
        responder = UrlResponder()
        config = MockEngineConfig()
        config.addHandler(responder)
        val engine = MockEngine(config)
        val network = NetworkModule()
        clientRoot = network.clientRoot(engine)
        clientData = network.clientData(clientRoot)
        clientCustomer = network.clientCustomer(clientRoot)
        prepare()
    }

}