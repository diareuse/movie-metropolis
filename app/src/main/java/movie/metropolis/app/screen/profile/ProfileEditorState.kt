package movie.metropolis.app.screen.profile

import androidx.compose.runtime.*

@Immutable
data class ProfileEditorState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val marketingMessaging: Boolean = false,
    private val original: ProfileEditorState? = null
) {

    val isChanged
        get() = original?.let {
            var out = false
            out = out or (firstName != it.firstName)
            out = out or (lastName != it.lastName)
            out = out or (email != it.email)
            out = out or (phone != it.phone)
            out = out or (marketingMessaging != it.marketingMessaging)
            out
        } ?: false

}