package movie.pulse

import android.content.Context
import androidx.work.Data

interface ExactPulse {

    fun execute(context: Context, data: Data)

}