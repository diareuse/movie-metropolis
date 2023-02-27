package movie.style

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
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
            .imagePlaceholder(isLoading || url == null, Theme.container.button)
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