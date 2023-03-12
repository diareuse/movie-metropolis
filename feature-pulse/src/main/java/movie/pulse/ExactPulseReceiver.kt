package movie.pulse

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import movie.pulse.di.ExactPulseEntryPoint
import movie.pulse.util.getSerializableExtraCompat
import java.util.Date
import kotlin.time.Duration.Companion.minutes

internal class ExactPulseReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent?.action != ActionPulse) return
        val di = ExactPulseEntryPoint(context)
        val exactPulses = di.pulses()
        val name = intent.getSerializableExtraCompat<Class<*>>(ExtraType)
        val data = intent.getByteArrayExtra(ExtraData)?.let(Data::fromByteArray) ?: Data.EMPTY
        val pulse = exactPulses.firstOrNull { it::class.java == name } ?: return
        pulse
            .runCatching { execute(context, data) }
            .onFailure {
                val offset = 15.minutes.inWholeMilliseconds
                val date = Date(System.currentTimeMillis() + offset)
                val request = ExactPulseRequest.Builder(intent)
                    .setDate(date)
                    .build()
                di.scheduler().schedule(request)
            }
    }

    companion object {

        const val ActionPulse = "movie.pulse.PULSE"
        const val ExtraType = "movie.pulse.EXTRA_TYPE"
        const val ExtraData = "movie.pulse.EXTRA_DATA"

    }

}

