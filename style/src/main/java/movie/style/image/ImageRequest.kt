package movie.style.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntSize
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Precision
import coil.size.Scale
import coil.size.Size
import kotlinx.coroutines.Dispatchers

@Composable
fun rememberImageRequest(
    model: Any?,
    size: IntSize = IntSize.Zero
): ImageRequest {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    return remember(model, size) {
        ImageRequest.Builder(context)
            .data(model)
            .diskCachePolicy(CachePolicy.ENABLED)
            .decoderDispatcher(Dispatchers.Default)
            .fetcherDispatcher(Dispatchers.IO.limitedParallelism(5))
            .interceptorDispatcher(Dispatchers.IO.limitedParallelism(5))
            .transformationDispatcher(Dispatchers.Default)
            .lifecycle(owner)
            .precision(Precision.INEXACT)
            .size(size)
            .scale(Scale.FILL)
            .crossfade(true)
            .build()
    }
}

private fun ImageRequest.Builder.size(size: IntSize) = when {
    size.width <= 0 || size.height <= 0 -> this
    else -> size(Size(size.width, size.height))
}
