package movie.style

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import movie.style.image.rememberImageRequest
import movie.style.theme.Theme

@Composable
fun AppImage(
    url: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    alignment: Alignment = Alignment.Center
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var isSuccess by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    val colorFilter =
        if (isSuccess || placeholder == null) null
        else ColorFilter.tint(LocalContentColor.current)
    AsyncImage(
        modifier = modifier
            .imagePlaceholder(isLoading, Theme.container.button)
            .onGloballyPositioned { size = it.size },
        model = rememberImageRequest(url, size),
        contentDescription = null,
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        colorFilter = colorFilter,
        contentScale = ContentScale.Crop,
        alignment = alignment,
        onError = { isLoading = false },
        onLoading = { isLoading = true },
        onSuccess = {
            isSuccess = true
            isLoading = false
        }
    )
}