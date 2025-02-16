package movie.style

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.core.content.edit
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.imageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.style.image.rememberImageRequest
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme
import movie.style.theme.contentColorFor
import movie.style.util.getLongArray
import movie.style.util.putLongArray
import movie.style.util.rememberSharedPrefs
import androidx.palette.graphics.Palette as AndroidPalette

@Composable
fun Image(
    state: ImageState,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    placeholderError: Painter? = painterResource(id = R.drawable.ic_image_error),
    alignment: Alignment = Alignment.Center
) {
    val context = LocalContext.current
    val loader = remember(context) { state.getLoader(context) }
    val request = if (state.url != null) rememberImageRequest(url = state.url) else null
    SubcomposeAsyncImage(
        modifier = modifier,
        model = request,
        contentDescription = contentDescription,
        imageLoader = loader,
        alignment = alignment,
        contentScale = ContentScale.Crop,
        onLoading = { state.processState(it) },
        onSuccess = { state.processState(it) },
        onError = { state.processState(it) },
        error = {
            if (placeholderError != null) Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = placeholderError,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(LocalContentColor.current)
                )
            }
            else SubcomposeAsyncImageContent()
        }
    )
}

@Composable
fun rememberImageState(url: Any?): ImageState {
    return remember(url) { HardwareImageState(url) }
}

fun Any?.hashString() = "%08x".format(hashCode())

private const val ImageRetentionStore = "image-retention-store"

@Composable
fun rememberPaletteImageState(
    url: Any?,
    color: Color = Theme.color.container.background,
    contentColor: Color = Theme.color.contentColorFor(color)
): PaletteImageState {
    val prefs = rememberSharedPrefs(name = ImageRetentionStore)
    val key = remember(url) { url.hashString() }
    val default = remember(color, contentColor) {
        longArrayOf(
            color.value.toLong(),
            contentColor.value.toLong(),
            contentColor.value.toLong()
        )
    }
    val imageState = rememberSaveable(url, default, saver = PaletteImageState.Saver) {
        val storedColors = prefs.getLongArray(key, default)
        val defaultPalette = PaletteImageState.Palette(storedColors)
        PaletteImageState(
            url = url,
            defaultPalette = defaultPalette,
            resolvePalette = url != null && storedColors.contentEquals(default)
        )
    }
    LaunchedEffect(imageState.palette) {
        if (imageState.hasNewPalette) withContext(Dispatchers.IO) {
            prefs.edit(commit = true) {
                putLongArray(key, imageState.palette.toLongArray())
            }
        }
    }
    return imageState
}

@Stable
sealed class ImageState {

    @Stable
    abstract val url: Any?

    open fun getLoader(context: Context) = context.imageLoader

    internal open fun processState(state: AsyncImagePainter.State) = Unit

}

@Stable
data class HardwareImageState(
    @Stable
    override val url: Any?
) : ImageState()

@Stable
data class PaletteImageState(
    @Stable
    override val url: Any?,
    private val defaultPalette: Palette,
    private val resolvePalette: Boolean
) : ImageState() {

    val hasNewPalette by derivedStateOf { defaultPalette != palette }

    var palette by mutableStateOf(defaultPalette)
        private set

    override fun getLoader(context: Context) = super.getLoader(context).newBuilder()
        .allowHardware(!resolvePalette)
        .build()

    override fun processState(state: AsyncImagePainter.State) {
        if (state !is AsyncImagePainter.State.Success || !resolvePalette) return
        AndroidPalette.from(state.result.drawable.toBitmap())
            .resizeBitmapArea(200)
            .generate {
                palette = Palette(it ?: return@generate)
            }
    }

    private fun Palette(palette: AndroidPalette): Palette {
        val swatch = palette.vibrantSwatch ?: palette.mutedSwatch ?: palette.dominantSwatch
        return Palette(
            color = Color(0xff000000 + (swatch?.rgb ?: 0)),
            textColor = Color(0xff000000 + (swatch?.bodyTextColor ?: 0)),
            titleColor = Color(0xff000000 + (swatch?.titleTextColor ?: 0))
        )
    }

    @Immutable
    data class Palette(
        val color: Color,
        val textColor: Color,
        val titleColor: Color
    ) {

        fun toLongArray() = longArrayOf(
            color.value.toLong(),
            textColor.value.toLong(),
            titleColor.value.toLong()
        )

        constructor(packed: LongArray) : this(
            color = Color(packed[0].toULong()),
            textColor = Color(packed[1].toULong()),
            titleColor = Color(packed[2].toULong())
        )

    }

    companion object {
        val Saver = listSaver(
            save = {
                listOf(
                    it.url,

                    it.defaultPalette.color.toArgb(),
                    it.defaultPalette.textColor.toArgb(),
                    it.defaultPalette.titleColor.toArgb(),

                    it.palette.color.toArgb(),
                    it.palette.textColor.toArgb(),
                    it.palette.titleColor.toArgb()
                )
            },
            restore = {
                val defaultPalette = Palette(
                    color = Color(it[1] as Int),
                    textColor = Color(it[2] as Int),
                    titleColor = Color(it[3] as Int)
                )
                val resolvedPalette = Palette(
                    color = Color(it[4] as Int),
                    textColor = Color(it[5] as Int),
                    titleColor = Color(it[6] as Int)
                )
                PaletteImageState(
                    url = it[0],
                    defaultPalette = defaultPalette,
                    resolvePalette = resolvedPalette == defaultPalette
                ).apply {
                    palette = resolvedPalette
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun ImagePreview() = PreviewLayout {
    Image(
        modifier = Modifier.size(64.dp),
        state = rememberImageState(url = "https://play-lh.googleusercontent.com/Iq5pH4PsEKfG8MXSJWl7380LAO4xtp6b9TZhqI5_92nfCmqO2uJEFzfxqf5YT8Fk3vY=w480-h960")
    )
}

@Preview(showBackground = true)
@Composable
private fun ImagePalettePreview() = PreviewLayout {
    val state =
        rememberPaletteImageState(url = "https://play-lh.googleusercontent.com/Iq5pH4PsEKfG8MXSJWl7380LAO4xtp6b9TZhqI5_92nfCmqO2uJEFzfxqf5YT8Fk3vY=w480-h960")
    val color = state.palette.color
    Image(
        modifier = Modifier
            .background(color)
            .size(64.dp)
            .padding(8.dp),
        state = state
    )
}