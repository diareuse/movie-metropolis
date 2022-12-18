package movie.core

import movie.core.model.MoviePreview
import movie.core.pulse.ExactPulseNotificationMovie
import movie.pulse.ExactPulseRequest
import movie.pulse.ExactPulseScheduler

class FavoriteFeatureScheduleNotification(
    private val origin: FavoriteFeature,
    private val scheduler: ExactPulseScheduler
) : FavoriteFeature by origin {

    override suspend fun toggle(
        movie: MoviePreview
    ) = origin.toggle(movie).onSuccess { isFavorite ->
        val request = ExactPulseRequest.Builder<ExactPulseNotificationMovie>()
            .setDate(movie.screeningFrom)
            .setData(ExactPulseNotificationMovie.getData(movie))
            .build()
        when (isFavorite) {
            true -> scheduler.schedule(request)
            else -> scheduler.cancel(request)
        }
    }

}