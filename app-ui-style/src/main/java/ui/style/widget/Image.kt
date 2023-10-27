package ui.style.widget

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ui.style.PreviewLayout
import androidx.palette.graphics.Palette as AndroidPalette

@Composable
fun Image(
    state: ImageState,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val loader = remember(context) { state.getLoader(context) }
    AsyncImage(
        modifier = modifier,
        model = state.url,
        imageLoader = loader,
        contentDescription = contentDescription,
        onState = {
            scope.launch {
                state.processState(it)
            }
        }
    )
}

@Composable
fun rememberImageState(url: String): ImageState {
    return remember(url) { HardwareImageState(url) }
}

@Composable
fun rememberPaletteImageState(url: String): PaletteImageState {
    return remember(url) { PaletteImageState(url) }
}

sealed class ImageState {

    abstract val url: String

    var state by mutableStateOf(LoadState.Loading)
        private set

    open fun getLoader(context: Context) = context.imageLoader

    internal open suspend fun processState(state: AsyncImagePainter.State) {
        this.state = when (state) {
            AsyncImagePainter.State.Empty,
            is AsyncImagePainter.State.Error -> LoadState.Failure

            is AsyncImagePainter.State.Loading -> LoadState.Loading
            is AsyncImagePainter.State.Success -> LoadState.Loaded
        }
    }

}

data class HardwareImageState(
    override val url: String
) : ImageState()

data class PaletteImageState(
    override val url: String
) : ImageState() {

    var palette by mutableStateOf(palettes[url])
        private set

    override fun getLoader(context: Context) = super.getLoader(context).newBuilder()
        .allowHardware(palette != null)
        .build()

    override suspend fun processState(state: AsyncImagePainter.State) {
        super.processState(state)
        if (state !is AsyncImagePainter.State.Success) return
        palette = palettes[url] ?: lock.withLock {
            palettes[url] ?: AndroidPalette.from(state.result.drawable.toBitmap())
                .resizeBitmapArea(200)
                .generate()
                .let(::Palette)
                .also {
                    palettes[url] = it
                    palette = it
                }
        }
    }

    private fun Palette(palette: AndroidPalette) = Palette(
        color = Color(0xff000000 + (palette.dominantSwatch?.rgb ?: 0)),
        textColor = Color(0xff000000 + (palette.dominantSwatch?.bodyTextColor ?: 0)),
        titleColor = Color(0xff000000 + (palette.dominantSwatch?.titleTextColor ?: 0))
    )

    data class Palette(
        val color: Color,
        val textColor: Color,
        val titleColor: Color
    )

    companion object {
        private val lock = Mutex()
        private val palettes = mutableMapOf<String, Palette>()
    }

}

enum class LoadState {
    Loading, Loaded, Failure
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
    val color = state.palette?.color ?: Color.Black
    Image(
        modifier = Modifier
            .background(color)
            .size(64.dp)
            .padding(8.dp),
        state = state
    )
}