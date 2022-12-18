package movie.pulse

import android.app.AlarmManager
import android.content.Context
import androidx.core.content.getSystemService

internal class ExactPulseSchedulerPlatform(
    private val context: Context
) : ExactPulseScheduler {

    private val manager = context.getSystemService<AlarmManager>().let(::checkNotNull)

    override fun schedule(request: ExactPulseRequest) {
        val intent = request.toPendingIntent(context)
        manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, request.at.time, intent)
    }

    override fun cancel(request: ExactPulseRequest) {
        val intent = request.toPendingIntent(context)
        manager.cancel(intent)
    }

}