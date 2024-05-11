package movie.core.auth

interface UserAccount {

    val isLoggedIn: Boolean
    var email: String?
    var password: String?

}