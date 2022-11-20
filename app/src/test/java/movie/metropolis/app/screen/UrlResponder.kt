package movie.metropolis.app.screen

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Url

typealias Responder = suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData

class UrlResponder : Responder {

    private val responses = mutableMapOf<Url, String>()

    fun onUrlRespond(url: Url, content: String) {
        responses[url] = content
    }

    override suspend fun invoke(p1: MockRequestHandleScope, p2: HttpRequestData): HttpResponseData {
        return p1.respondOk(responses.getValue(p2.url))
    }

}