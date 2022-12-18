package movie.metropolis.app.screen.listing

import movie.core.EventFeature
import movie.core.FavoriteFeature
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewFromFeature
import movie.metropolis.app.screen.OnChangedListener
import movie.metropolis.app.screen.cinema.Listenable

class ListingFacadeFromFeature(
    private val event: EventFeature,
    private val favorite: FavoriteFeature
) : ListingFacade {

    private val favoriteListenable = Listenable<OnChangedListener>()

    override suspend fun getCurrent(): Result<List<MovieView>> {
        val values = event.getCurrent().getOrThrow()
            .map { MovieViewFromFeature(it, favorite.isFavorite(it).getOrThrow(), true) }
        return Result.success(values)
    }

    override suspend fun getUpcoming(): Result<List<MovieView>> {
        val values = event.getUpcoming().getOrThrow()
            .map { MovieViewFromFeature(it, favorite.isFavorite(it).getOrThrow(), false) }
        return Result.success(values)
    }

    override suspend fun toggleFavorite(movie: MovieView) {
        if (movie !is MovieViewFromFeature) return
        favorite.toggle(movie.movie)
        favoriteListenable.notify { onChanged() }
    }

    override fun addOnFavoriteChangedListener(listener: OnChangedListener): OnChangedListener {
        favoriteListenable += listener
        return listener
    }

    override fun removeOnFavoriteChangedListener(listener: OnChangedListener) {
        favoriteListenable -= listener
    }

}