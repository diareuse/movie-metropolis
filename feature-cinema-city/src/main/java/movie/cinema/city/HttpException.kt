package movie.cinema.city

import java.io.IOException

class HttpException(
    val status: Int,
    val url: String,
    val body: String
) : IOException() {
    override val message: String
        get() = "$status $url\n$body"
}