package movie.style

import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.painter.*
import androidx.compose.ui.res.*

/*@Deprecated("", level = DeprecationLevel.ERROR)
@Composable
fun AppImage(
    url: String?,
    alignment: Alignment = Alignment.Center
) = AppImage(
    //modifier = modifier.imagePlaceholder(url == null),
    url = url.orEmpty(),
    //placeholder = placeholder,
    placeholderError = painterResource(id = R.drawable.ic_image_error),
    alignment = alignment
)*/

@Deprecated("", level = DeprecationLevel.ERROR)
@Composable
fun AppImage(
    url: String,
    placeholderError: Painter? = painterResource(id = R.drawable.ic_image_error),
    alignment: Alignment = Alignment.Center
) {
    Image(
        state = rememberImageState(url = url),
        placeholderError = placeholderError,
        alignment = alignment
    )
}