package movie.pulse

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

abstract class AbstractPulseWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    abstract val pulse: Pulse

    override suspend fun doWork() = pulse.execute().fold(
        onSuccess = { Result.success() },
        onFailure = { Result.retry() }
    )

}