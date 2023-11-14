package movie.style.image

import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Precision
import coil.size.Scale

@Composable
fun rememberImageRequest(url: Any?): ImageRequest {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    return remember(url) {
        ImageRequest.Builder(context)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .lifecycle(owner)
            .precision(Precision.INEXACT)
            .scale(Scale.FILL)
            .crossfade(true)
            .build()
    }
}
