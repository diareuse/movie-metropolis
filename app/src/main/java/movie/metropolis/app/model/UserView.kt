package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface UserView {

    val firstName: String
    val lastName: String
    val email: String
    val phone: String
    val favorite: CinemaSimpleView?
    val consent: ConsentView

    @Stable
    interface ConsentView {
        val marketing: Boolean
        val premium: Boolean
    }

}

