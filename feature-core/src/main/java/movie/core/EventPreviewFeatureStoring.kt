package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import movie.core.adapter.asStored
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.model.Movie
import movie.core.model.MoviePreview
import movie.core.nwk.model.ShowingType

class EventPreviewFeatureStoring(
    private val origin: EventPreviewFeature,
    private val type: ShowingType,
    private val movie: MovieDao,
    private val preview: MoviePreviewDao,
    private val media: MovieMediaDao,
    private val scope: CoroutineScope
) : EventPreviewFeature {

    override suspend fun get(): Result<Sequence<MoviePreview>> {
        return origin.get().onSuccess {
            scope.launch {
                store(it)
            }
        }
    }

    private suspend fun store(movies: Sequence<MoviePreview>) {
        val upcoming = type == ShowingType.Upcoming
        preview.deleteAll(upcoming = upcoming)
        for (item in movies) {
            movie.insertOrUpdate((item as Movie).asStored())
            preview.insertOrUpdate(item.asStored(upcoming = upcoming))
            for (mediaItem in item.media)
                media.insertOrUpdate(mediaItem.asStored(item))
        }
    }

}