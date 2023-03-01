package movie.metropolis.app.screen.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.style.AppImage
import movie.style.OnClickListener
import movie.style.layout.CutoutLayout
import movie.style.layout.PreviewLayout
import movie.style.modifier.optional
import movie.style.modifier.surface
import movie.style.theme.Theme

@Composable
fun CinemaLayout(
    image: @Composable () -> Unit,
    city: @Composable () -> Unit,
    name: @Composable () -> Unit,
    address: @Composable () -> Unit,
    distance: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: OnClickListener? = null
) {
    CutoutLayout(
        modifier = modifier.wrapContentHeight(unbounded = true),
        color = Theme.color.container.primary,
        shape = Theme.container.button,
        overlay = distance
    ) {
        Column(
            modifier = Modifier
                .optional(onClick) { clickable(onClick = it) }
                .surface(tonalElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .surface(
                        Theme.color.container.surface,
                        Theme.container.card.copy(
                            topStart = CornerSize(0f),
                            topEnd = CornerSize(0f)
                        )
                    )
            ) {
                image()
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                ProvideTextStyle(Theme.textStyle.title) {
                    name()
                }
                ProvideTextStyle(Theme.textStyle.caption) {
                    city()
                    address()
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun CinemaLayoutPreview(
    @PreviewParameter(CinemaLayoutParameter::class, 2) parameter: CinemaLayoutParameter.Data
) = PreviewLayout {
    CinemaLayout(
        image = { AppImage(parameter.image) },
        city = { Text(parameter.city) },
        name = { Text(parameter.name) },
        address = { Text(parameter.address) },
        distance = {
            if (parameter.distance != null)
                Text(parameter.distance, modifier = Modifier.padding(16.dp, 8.dp))
        }
    )
}

private class CinemaLayoutParameter :
    CollectionPreviewParameterProvider<CinemaLayoutParameter.Data>(
        listOf(Data(), Data(distance = "2.3km"))
    ) {
    data class Data(
        val image: String = "https://www.cinemacity.cz/static/dam/jcr:7d2db264-ff3d-417b-b88d-0d8f95a6ca82",
        val city: String = "Prague",
        val name: String = "Cinema City",
        val address: String = "My address 1/123",
        val distance: String? = null
    )
}