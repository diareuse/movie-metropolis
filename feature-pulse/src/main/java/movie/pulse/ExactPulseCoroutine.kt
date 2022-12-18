package movie.pulse

import android.content.Context
import androidx.work.Data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class ExactPulseCoroutine : ExactPulse {

    final override fun execute(context: Context, data: Data) {
        val scope = CoroutineScope(SupervisorJob())
        scope.launch { executeAsync(context, data) }
    }

    abstract suspend fun executeAsync(context: Context, data: Data)

}