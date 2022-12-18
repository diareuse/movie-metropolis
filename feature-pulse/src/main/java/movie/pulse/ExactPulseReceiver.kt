package movie.pulse

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Data
import movie.pulse.di.ExactPulseEntryPoint

internal class ExactPulseReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent?.action != ActionPulse) return
        val exactPulses = ExactPulseEntryPoint(context).pulses()
        val name = intent.type()
        val data = intent.getByteArrayExtra(ExtraData)?.let(Data::fromByteArray) ?: Data.EMPTY
        val pulse = exactPulses.firstOrNull { it::class.java == name } ?: return
        pulse.execute(context, data)
    }

    private fun Intent.type() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(ExtraType, Class::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(ExtraType) as? Class<*>
    }

    companion object {

        const val ActionPulse = "movie.pulse.PULSE"
        const val ExtraType = "movie.pulse.EXTRA_TYPE"
        const val ExtraData = "movie.pulse.EXTRA_DATA"

    }

}

