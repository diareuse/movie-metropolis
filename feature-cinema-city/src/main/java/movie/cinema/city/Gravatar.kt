package movie.cinema.city

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.security.MessageDigest

suspend fun gravatarURL(email: String): URL {
    val digest = withContext(Dispatchers.Default) {
        MessageDigest.getInstance("MD5")
            .digest(email.lowercase().encodeToByteArray())
            .joinToString("") { "%02x".format(it) }
    }
    return URL("https://www.gravatar.com/avatar/$digest?s=256&d=404")
}