package movie.metropolis.app.presentation.order

data class RequestView(
    val url: String,
    val headers: Map<String, String>
)