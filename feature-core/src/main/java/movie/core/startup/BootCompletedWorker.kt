package movie.core.startup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import movie.core.FavoriteFeature
import movie.core.model.MoviePreview
import movie.core.pulse.ExactPulseNotificationMovie
import movie.pulse.ExactPulseRequest
import movie.pulse.ExactPulseScheduler
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedWorker : BroadcastReceiver() {

    @Inject
    lateinit var favorite: FavoriteFeature

    @Inject
    lateinit var scheduler: ExactPulseScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        val scope = CoroutineScope(SupervisorJob())
        scope.launch {
            favorite.getAll().onSuccess {
                for (movie in it.map(::asRequest))
                    scheduler.schedule(movie)
            }
        }
    }

    private fun asRequest(movie: MoviePreview) =
        ExactPulseRequest.Builder<ExactPulseNotificationMovie>()
            .setDate(movie.screeningFrom)
            .setData(ExactPulseNotificationMovie.getData(movie))
            .build()

}