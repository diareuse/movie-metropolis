package movie.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import movie.core.nwk.EndpointProvider
import movie.core.nwk.PerformanceTracerNoop
import movie.core.nwk.di.NetworkModule
import movie.log.Logger
import movie.log.PlatformLogger
import org.junit.Before

abstract class FeatureTest {

    private lateinit var tracer: PerformanceTracerNoop
    protected lateinit var config: MockEngineConfig
    protected lateinit var clientRoot: HttpClient
    protected lateinit var clientData: HttpClient
    protected lateinit var clientQuickbook: HttpClient
    protected lateinit var clientCustomer: HttpClient
    protected lateinit var responder: UrlResponder

    abstract fun prepare()

    @Before
    fun prepareInternal() {
        Logger.setLogger(PlatformLogger())
        responder = UrlResponder()
        tracer = PerformanceTracerNoop
        config = MockEngineConfig()
        config.addHandler(responder)
        val engine = MockEngine(config)
        val network = NetworkModule()
        val provider = Provider()
        clientRoot = network.clientRoot(engine, provider, tracer)
        clientData = network.clientData(clientRoot, provider)
        clientQuickbook = network.clientQuickbook(clientRoot, provider)
        clientCustomer = network.clientCustomer(clientRoot, provider)
        prepare()
    }

    private class Provider : EndpointProvider {
        override val domain: String = "https://www.cinemacity.cz"
        override val id: Int = 10101
    }

}