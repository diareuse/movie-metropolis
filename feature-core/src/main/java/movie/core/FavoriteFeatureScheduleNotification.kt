package movie.core

import movie.core.model.Movie
import movie.core.pulse.ExactPulseNotificationMovie
import movie.pulse.ExactPulseRequest
import movie.pulse.ExactPulseScheduler

class FavoriteFeatureScheduleNotification(
    private val origin: FavoriteFeature,
    private val scheduler: ExactPulseScheduler
) : FavoriteFeature by origin {

    override suspend fun toggle(
        movie: Movie
    ) = origin.toggle(movie).onSuccess { isFavorite ->
        val request = ExactPulseRequest.Builder<ExactPulseNotificationMovie>()
            .setDate(movie.screeningFrom)
            .setData(ExactPulseNotificationMovie.getData(movie))
            .build()
        println("isfav: $isFavorite")
        when (isFavorite && get(movie).onFailure { it.printStackTrace() }
            .map { println("notified:${it.isNotified}"); !it.isNotified }
            .getOrDefault(false)) {
            true -> scheduler.schedule(request)
            else -> scheduler.cancel(request)
        }
    }

}