package movie.metropolis.app.model

sealed class ProjectionType {
    data object Imax : ProjectionType()
    data object Plane2D : ProjectionType()
    data object Plane3D : ProjectionType()
    data object Plane4DX : ProjectionType()
    data object DolbyAtmos : ProjectionType()
    data object HighFrameRate : ProjectionType()
    data object VIP : ProjectionType()
    data class Other(val type: String) : ProjectionType()

    companion object {
        operator fun invoke(value: String) = when (value.lowercase()) {
            "imax" -> Imax
            "2d" -> Plane2D
            "3d" -> Plane3D
            "4dx" -> Plane4DX
            "dolby atmos" -> DolbyAtmos
            "high frame rate" -> HighFrameRate
            "vip" -> VIP
            else -> Other(value)
        }
    }
}