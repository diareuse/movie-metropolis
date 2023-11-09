package movie.core.startup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import movie.core.di.BootCompletedWorkerEntryPoint
import movie.core.model.MoviePreview
import movie.core.pulse.ExactPulseNotificationMovie
import movie.pulse.ExactPulseRequest

class BootCompletedWorker : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val entry = BootCompletedWorkerEntryPoint(context)
        scope.launch {
            entry.favorite().getAll().onSuccess {
                for (movie in it.map(::asRequest))
                    entry.scheduler().schedule(movie)
            }
        }
    }

    private fun asRequest(movie: MoviePreview) =
        ExactPulseRequest.Builder<ExactPulseNotificationMovie>()
            .setDate(movie.screeningFrom)
            .setData(ExactPulseNotificationMovie.getData(movie))
            .build()

}