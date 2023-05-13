package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.screen.detail.component.ShowingItemTimeParameter.Data
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun ShowingItemTime(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    time: @Composable () -> Unit
) {
    SuggestionChip(
        modifier = modifier,
        onClick = onClick,
        label = time,
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = Theme.color.container.secondary,
            labelColor = Theme.color.content.secondary
        ),
        border = null
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ShowingItemTimePreview(
    @PreviewParameter(ShowingItemTimeParameter::class)
    parameter: Data
) = PreviewLayout {
    ShowingItemTime {
        Text(parameter.time)
    }
}

private class ShowingItemTimeParameter :
    CollectionPreviewParameterProvider<Data>(listOf(Data())) {
    data class Data(val time: String = "12:45")
}