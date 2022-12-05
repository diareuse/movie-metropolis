package movie.metropolis.app.model

interface UserView {

    val firstName: String
    val lastName: String
    val email: String
    val phone: String
    val favorite: CinemaSimpleView?
    val consent: ConsentView

    interface ConsentView {
        val marketing: Boolean
        val premium: Boolean
    }

}

