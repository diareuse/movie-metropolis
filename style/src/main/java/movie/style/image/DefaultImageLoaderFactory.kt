package movie.style.image

import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy

internal class DefaultImageLoaderFactory(
    private val context: Context
) : ImageLoaderFactory {

    override fun newImageLoader() = ImageLoader.Builder(context)
        .allowHardware(true)
        .allowRgb565(true)
        .crossfade(true)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .respectCacheHeaders(false)
        .build()

}