package movie.core.nwk

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineCapability
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.util.InternalAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

class LazyHttpClientEngine(
    factory: () -> HttpClientEngine
) : HttpClientEngine {

    private val origin by lazy(LazyThreadSafetyMode.SYNCHRONIZED, factory)

    override val config: HttpClientEngineConfig
        get() = origin.config

    override val dispatcher: CoroutineDispatcher
        get() = origin.dispatcher

    override val supportedCapabilities: Set<HttpClientEngineCapability<*>>
        get() = origin.supportedCapabilities

    override val coroutineContext: CoroutineContext
        get() = origin.coroutineContext

    @InternalAPI
    override fun install(client: HttpClient) {
        origin.install(client)
    }

    @InternalAPI
    override suspend fun execute(data: HttpRequestData): HttpResponseData {
        return origin.execute(data)
    }

    override fun close() {
        origin.close()
    }

}