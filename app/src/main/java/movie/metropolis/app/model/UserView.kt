package movie.metropolis.app.model

import androidx.compose.runtime.*

@Stable
data class UserView(
    val email: String
) {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")
    var favorite by mutableStateOf(null as CinemaSimpleView?)
    val consent = ConsentView()

    @Stable
    class ConsentView {
        var marketing by mutableStateOf(false)
        var premium by mutableStateOf(false)
    }

}

