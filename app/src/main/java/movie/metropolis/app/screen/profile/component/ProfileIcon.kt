package movie.metropolis.app.screen.profile.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.ripple.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.metropolis.app.R
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState
import java.security.MessageDigest

@Composable
fun ProfileIcon(
    email: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val image by rememberUserImage(email)
    val state = rememberImageState(url = image)
    Image(
        modifier = modifier
            .let {
                if (onClick == null) it
                else {
                    val interactionSource = remember { MutableInteractionSource() }
                    val indication = rememberRipple(bounded = false)
                    it.clickable(
                        onClick = onClick,
                        role = Role.Image,
                        interactionSource = interactionSource,
                        indication = indication
                    )
                }
            }
            .clip(CircleShape),
        state = state,
        placeholderError = painterResource(id = R.drawable.ic_user)
    )
}

@Composable
fun rememberUserImage(email: String): State<String> {
    val url = remember { mutableStateOf("") }
    LaunchedEffect(key1 = email) {
        val digest = withContext(Dispatchers.Default) {
            MessageDigest.getInstance("MD5")
                .digest(email.lowercase().encodeToByteArray())
                .joinToString("") { "%02x".format(it) }
        }
        url.value = "https://www.gravatar.com/avatar/$digest?s=256&d=404"
    }
    return url
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ProfileIconPreview() = PreviewLayout {
    ProfileIcon("example@exampl.org")
}