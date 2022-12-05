package movie.core.nwk

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import java.io.IOException

@Throws(NetworkException::class)
internal suspend inline fun <reified T> HttpResponse.requireBody(): T {
    if (status.isSuccess())
        return body()

    throw NetworkException(status.value, bodyAsText())
}

data class NetworkException(
    val code: Int,
    val responseBody: String
) : IOException()