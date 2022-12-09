package movie.metropolis.app.screen.order

data class RequestView(
    val url: String,
    val headers: Map<String, String>
)