package movie.metropolis.app.presentation.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import movie.metropolis.app.model.MovieView

class FavoriteFacadeCinemaCity : FavoriteFacade {

    override fun get(): Flow<List<MovieView>> = emptyFlow()

    override suspend fun remove(view: MovieView) {
        //TODO("Not yet implemented")
    }

}