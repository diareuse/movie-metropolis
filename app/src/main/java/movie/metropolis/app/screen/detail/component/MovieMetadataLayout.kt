package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.style.layout.CutoutLayout
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun MovieMetadataLayout(
    aspectRatio: Float,
    color: Color,
    rating: @Composable () -> Unit,
    image: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        CutoutLayout(
            color = color,
            shape = Theme.container.poster,
            modifier = Modifier
                .fillMaxWidth(.3f)
                .aspectRatio(aspectRatio),
            overlay = rating
        ) {
            image()
        }
        Box(Modifier.weight(1f)) {
            content()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MovieMetadataLayoutPreview() = PreviewLayout {
    MovieMetadataLayout(
        aspectRatio = DefaultPosterAspectRatio,
        color = Color.Magenta,
        rating = { Text("75%", Modifier.padding(12.dp, 8.dp)) },
        image = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Magenta)
            )
        }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Gray)
        )
    }
}