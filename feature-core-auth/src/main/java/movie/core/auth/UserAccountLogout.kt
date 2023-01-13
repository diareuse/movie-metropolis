package movie.core.auth

class UserAccountLogout(
    private val origin: UserAccount
) : UserAccount by origin {

    override val isLoggedIn: Boolean
        get() {
            val isLoggedIn = origin.isLoggedIn
            if (isLoggedIn) {
                email = null
            }
            return isLoggedIn
        }

    override var email: String?
        get() = origin.email?.takeIf { isLoggedIn }
        set(value) {
            origin.email = value
        }

}