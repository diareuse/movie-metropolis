package movie.pulse

import android.content.Context
import androidx.startup.Initializer
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class PulseStartup : Initializer<Unit> {

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    override fun create(context: Context) {
        val manager = WorkManager.getInstance(context)
        val daily = createDailyRequest()
        manager.enqueueUniquePeriodicWork("daily", ExistingPeriodicWorkPolicy.KEEP, daily)
    }

    private fun createDailyRequest() =
        PeriodicWorkRequestBuilder<PulseDailyWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            .build()

    override fun dependencies() = listOf(WorkManagerStartup::class.java)

}