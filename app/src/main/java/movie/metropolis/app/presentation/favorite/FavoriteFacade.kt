package movie.metropolis.app.presentation.favorite

import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.MovieView

interface FavoriteFacade {

    fun get(): Flow<List<MovieView>>
    suspend fun remove(view: MovieView)

}