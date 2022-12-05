package movie.metropolis.app.util

import kotlin.random.Random.Default.nextBytes

fun nextString(length: Int = 10) = String(nextBytes(length))