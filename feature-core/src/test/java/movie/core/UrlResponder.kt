package movie.core

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headersOf

typealias Responder = suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData

class UrlResponder : Responder {

    private val responses =
        mutableMapOf<HttpMethod, MutableMap<Url, Pair<HttpStatusCode, String>>>()

    @Deprecated("")
    fun onUrlRespond(url: Url, name: String) {
        onUrlRespond(HttpMethod.Get, url, name)
        onUrlRespond(HttpMethod.Post, url, name)
    }

    @Deprecated("")
    fun onUrlRespond(method: HttpMethod, url: Url, name: String) = on(url) {
        this.method = method
        this.code = HttpStatusCode.OK
        fileAsBody(name)
    }

    fun on(url: Url, builder: UrlResponseBuilder.() -> Unit) {
        UrlResponseBuilder(url).apply(builder).buildInto(responses)
    }

    override suspend fun invoke(p1: MockRequestHandleScope, p2: HttpRequestData): HttpResponseData {
        val (code, body) = requireNotNull(responses[p2.method]?.get(p2.url)) {
            "${p2.method} | ${p2.url} has no response"
        }
        return p1.respond(
            content = body,
            status = code,
            headers = headersOf("content-type", "application/json")
        )
    }

    companion object {

        private const val Domain = "https://www.cinemacity.cz"
        private const val DataService = "data-api-service"
        private const val CustomerService = "group-customer-service"

        val Cinema =
            Url("$Domain/mrest/cinema?lang=en")

        fun EventOccurrence(cinema: String, date: String) =
            Url("$Domain/cz/$DataService/v1/quickbook/10101/film-events/in-cinema/${cinema}/at-date/${date}")

        fun CinemaLocation(lat: Double, lng: Double) =
            Url("$Domain/cz/$DataService/v1/10101/cinema/bylocation?lat=${lat}&long=${lng}&unit=KILOMETERS")

        fun Detail(id: String = "5376O2R") =
            Url("$Domain/cz/$DataService/v1/10101/films/byDistributorCode/${id}?lang=en")

        fun MoviesByShowing(type: String) =
            Url("$Domain/cz/$DataService/v1/10101/films/by-showing-type/${type}?ordering=asc&lang=en")

        val Password =
            Url("$Domain/cz/$CustomerService/v1/password?reCaptcha=captcha")

        val Register =
            Url("$Domain/cz/$CustomerService/v1/customers?reCaptcha=captcha")

        val Customer =
            Url("$Domain/cz/$CustomerService/v1/customers/current")

        val CustomerPoints =
            Url("$Domain/cz/$CustomerService/v1/customer/points")

        val Auth =
            Url("$Domain/cz/$CustomerService/oauth/token")

        val Booking =
            Url("$Domain/cz/$CustomerService/v1/bookings?lang=en")

        fun Booking(id: String = "1") =
            Url("$Domain/cz/$CustomerService/v1/bookings/${id}?lang=en")

    }

}

class UrlResponseBuilder(
    private val url: Url
) {

    var code: HttpStatusCode = HttpStatusCode.OK
    var method: HttpMethod = HttpMethod.Get
    lateinit var body: String

    fun fileAsBody(name: String) {
        body = Thread.currentThread().contextClassLoader
            ?.getResourceAsStream(name)
            ?.use { it.readBytes() }
            ?.let(::String).orEmpty()
    }

    fun buildInto(map: MutableMap<HttpMethod, MutableMap<Url, Pair<HttpStatusCode, String>>>) {
        map.getOrPut(method) { mutableMapOf() }[url] = code to body
    }

}