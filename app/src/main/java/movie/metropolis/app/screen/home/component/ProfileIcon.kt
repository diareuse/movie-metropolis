package movie.metropolis.app.screen.home.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.style.AppIconButton
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState
import java.security.MessageDigest

@Composable
fun ProfileIcon(
    email: String,
    onClick: () -> Unit
) {
    AppIconButton(onClick = onClick) {
        val image by rememberUserImage(email)
        val state = rememberImageState(url = image)
        Image(state)
    }
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
        url.value = "https://www.gravatar.com/avatar/$digest"
    }
    return url
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ProfileIconPreview() = PreviewLayout {
    ProfileIcon("example@exampl.org", {})
}