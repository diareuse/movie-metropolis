package movie.metropolis.app.screen.profile

data class ProfilePasswordState(
    val current: String = "",
    val new: String = ""
) {

    val isChanged get() = current.isNotEmpty() && new.isNotEmpty()

}