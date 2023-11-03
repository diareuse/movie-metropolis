package movie.style.action

import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.core.net.toUri

@Composable
fun actionView(link: () -> String): () -> Unit {
    val context = LocalContext.current
    return {
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(link().toUri())
            .let { Intent.createChooser(it, "") }
        context.startActivity(intent)
    }
}