package movie.core.pulse

import android.content.Context
import android.content.Intent
import movie.core.startup.BootCompletedWorker
import movie.pulse.Pulse

class PulseScheduling(
    private val context: Context
) : Pulse {

    override suspend fun execute(): Result<Unit> {
        val intent = Intent(context, BootCompletedWorker::class.java)
        context.sendBroadcast(intent)
        return Result.success(Unit)
    }

}