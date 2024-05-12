package movie.metropolis.app.util

import java.util.Date
import kotlin.time.Duration

operator fun Date.plus(duration: Duration) = Date(time + duration.inWholeMilliseconds)