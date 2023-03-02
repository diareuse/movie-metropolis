package movie.style

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import coil.compose.AsyncImage
import movie.style.image.rememberImageRequest

@Composable
fun AppImage(
    url: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    alignment: Alignment = Alignment.Center
) = AppImage(
    modifier = modifier.imagePlaceholder(url == null),
    url = url.orEmpty(),
    placeholder = placeholder,
    placeholderError = painterResource(id = R.drawable.ic_image_error),
    alignment = alignment
)

@Composable
fun AppImage(
    url: String,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    placeholderError: Painter? = painterResource(id = R.drawable.ic_image_error),
    alignment: Alignment = Alignment.Center
) {
    var isSuccess by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    val colorFilter =
        if (isSuccess || placeholder == null) null
        else ColorFilter.tint(LocalContentColor.current)
    AsyncImage(
        modifier = modifier.imagePlaceholder(isLoading),
        model = rememberImageRequest(url),
        contentDescription = null,
        placeholder = placeholder,
        error = placeholderError,
        fallback = placeholderError,
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