package movie.metropolis.app.model

data class ListingView(
    val items: List<MovieView>,
    val promotions: List<MovieView> = items.take(3)
)