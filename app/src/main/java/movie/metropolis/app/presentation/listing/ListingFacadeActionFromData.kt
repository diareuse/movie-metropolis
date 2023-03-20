package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.core.model.MoviePreview
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewFromFeature

data class ListingFacadeActionFromData(
    val data: Iterable<MoviePreview>
) : ListingFacade.Action {

    override val promotions: Flow<Result<List<MovieView>>> = flow {
        val items = data.take(3).map { MovieViewFromFeature(it, false) }
        emit(Result.success(items))
    }

    override val groups: Flow<Result<Map<Genre, List<MovieView>>>> = flow {
        val groups = mutableMapOf<Genre, MutableList<MoviePreview>>()
        for (item in data) {
            var count = 0
            for (genre in item.genres) {
                count++
                groups.getOrPut(Genre(genre)) { mutableListOf() } += item
            }
            if (count == 0) {
                groups.getOrPut(Genre("other")) { mutableListOf() } += item
            }
        }
        val out = groups
            .mapValues { (_, values) -> values.map { MovieViewFromFeature(it, false) } }
        emit(Result.success(out))
    }

}