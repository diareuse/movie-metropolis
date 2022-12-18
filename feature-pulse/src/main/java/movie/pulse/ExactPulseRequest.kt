package movie.pulse

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Data
import java.util.Date

class ExactPulseRequest private constructor(
    val type: Class<out ExactPulse>,
    val at: Date,
    val data: Data
) {

    fun toPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ExactPulseReceiver::class.java)
            .setAction(ExactPulseReceiver.ActionPulse)
            .putExtra(ExactPulseReceiver.ExtraType, type)
            .putExtra(ExactPulseReceiver.ExtraData, data.toByteArray())
        val requestCode = data.hashCode()
        return PendingIntent.getBroadcast(context, requestCode, intent, Flags)
    }

    class Builder(
        private val type: Class<out ExactPulse>
    ) {

        private lateinit var date: Date
        private var data: Data = Data.EMPTY

        fun setDate(date: Date) = apply {
            this.date = date
        }

        fun setData(data: Data) = apply {
            this.data = data
        }

        fun build() = ExactPulseRequest(type, date, data)

        companion object {

            inline operator fun <reified T : ExactPulse> invoke() = Builder(T::class.java)

        }

    }

    companion object {

        private const val Flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT

    }

}