package movie.metropolis.app.screen.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.detail.component.ShowingItemTime
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun ShowingTypeLayout(
    modifier: Modifier = Modifier,
    type: @Composable RowScope.() -> Unit,
    language: @Composable RowScope.() -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp),
    content: LazyListScope.() -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(Theme.textStyle.body) {
                type()
                Text("•")
                language()
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding,
            content = content
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ShowingTypeLayoutPreview(
    @PreviewParameter(ShowingTypeLayoutParameter::class, 1)
    parameter: ShowingTypeLayoutParameter.Data
) = PreviewLayout {
    ShowingTypeLayout(
        modifier = Modifier.background(Theme.color.container.surface),
        type = { Text(parameter.type) },
        language = { Text(parameter.language) },
        content = {
            items(parameter.times, { it }) {
                ShowingItemTime { Text(it) }
            }
        }
    )
}

private class ShowingTypeLayoutParameter :
    CollectionPreviewParameterProvider<ShowingTypeLayoutParameter.Data>(listOf(Data())) {
    data class Data(
        val type: String = "3D | IMAX",
        val language: String = "English",
        val times: List<String> = listOf("11:30", "13:00", "14:30")
    )
}