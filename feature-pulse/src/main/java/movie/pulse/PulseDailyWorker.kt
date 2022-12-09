package movie.pulse

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import movie.pulse.di.Daily

@HiltWorker
class PulseDailyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    @Daily override val pulse: Pulse
) : AbstractPulseWorker(context, params)