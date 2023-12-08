package movie.metropolis.app.model

@JvmInline
value class DiskSpace(val bytes: Long) {

    val kiloBytes get() = bytes / 1000F
    val megaBytes get() = kiloBytes / 1000F

    companion object {
        val Long.bytes get() = DiskSpace(this)
    }

}