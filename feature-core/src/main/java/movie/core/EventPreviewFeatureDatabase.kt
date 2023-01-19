package movie.core

import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.model.MoviePreview
import movie.core.nwk.model.ShowingType

class EventPreviewFeatureDatabase(
    private val preview: MoviePreviewDao,
    private val media: MovieMediaDao,
    private val type: ShowingType
) : EventPreviewFeature {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) {
        val output = when (type) {
            ShowingType.Current -> preview.selectCurrent()
            ShowingType.Upcoming -> preview.selectUpcoming()
        }.map { MoviePreviewFromDatabase(it, media.select(it.id)) }
        result(Result.success(output))
    }

}