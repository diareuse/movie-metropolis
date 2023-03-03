package movie.core.nwk

import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LazyHttpClient(factory: () -> HttpClient) {

    private val client by lazy(LazyThreadSafetyMode.SYNCHRONIZED, factory)

    suspend fun getOrCreate() = withContext(Dispatchers.IO) {
        client
    }

    fun modify(body: HttpClient.() -> HttpClient) = LazyHttpClient {
        body(client)
    }

}