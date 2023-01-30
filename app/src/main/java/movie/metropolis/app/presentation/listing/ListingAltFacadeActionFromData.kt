package movie.metropolis.app.presentation.listing

import movie.core.ResultCallback
import movie.core.model.MoviePreview
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewFromFeature

data class ListingAltFacadeActionFromData(
    val data: List<MoviePreview>
) : ListingAltFacade.Action {

    override suspend fun promotions(callback: ResultCallback<List<MovieView>>) {
        val data = data.take(3).map { MovieViewFromFeature(it, false) }
        callback(Result.success(data))
    }

    override suspend fun groupUp(callback: ResultCallback<Map<Genre, List<MovieView>>>) {
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
        callback(Result.success(out))
    }

}