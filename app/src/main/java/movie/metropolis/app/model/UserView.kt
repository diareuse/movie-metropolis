package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface UserView {

    val firstName: String
    val lastName: String
    val email: String
    val phone: String
    val favorite: CinemaSimpleView?
    val consent: ConsentView

    @Immutable
    interface ConsentView {
        val marketing: Boolean
        val premium: Boolean
    }

}

