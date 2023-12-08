package movie.metropolis.app.screen.space

import movie.metropolis.app.model.DiskSpace
import movie.metropolis.app.model.DiskSpace.Companion.bytes

data class SpaceState(
    val posters: DiskSpace = 0L.bytes,
    val movies: DiskSpace = 0L.bytes,
    val ratings: DiskSpace = 0L.bytes
)