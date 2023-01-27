package movie.metropolis.app.presentation.listing

import movie.core.EventPreviewFeature
import movie.core.FavoriteFeature
import movie.core.ResultCallback
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewFromFeature
import movie.metropolis.app.presentation.Listenable
import movie.metropolis.app.presentation.OnChangedListener

class ListingFacadeFromFeature(
    preview: EventPreviewFeature.Factory,
    private val favorite: FavoriteFeature
) : ListingFacade {

    private val favoriteListenable = Listenable<OnChangedListener>()
    private val current = preview.current()
    private val upcoming = preview.upcoming()

    override suspend fun getCurrent(callback: ResultCallback<List<MovieView>>) {
        current.getMovies(callback)
    }

    override suspend fun getUpcoming(callback: ResultCallback<List<MovieView>>) {
        upcoming.getMovies(callback)
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

    // ---

    private suspend fun EventPreviewFeature.getMovies(
        callback: ResultCallback<List<MovieView>>
    ) {
        get { result ->
            var output = result.map { movies ->
                movies.map {
                    MovieViewFromFeature(it, false)
                }
            }
            callback(output)
            output = result.map { movies ->
                movies.map {
                    val isFavorite = favorite.isFavorite(it).getOrDefault(false)
                    MovieViewFromFeature(it, isFavorite)
                }
            }
            callback(output)
        }
    }

}