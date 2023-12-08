package movie.metropolis.app.model

data class DeleteProgress(
    val deleted: Int,
    val max: Int
) {

    val progress get() = 1f * deleted / max

    operator fun inc() = copy(deleted = deleted + 1)

}