package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.Filter
import movie.style.layout.PreviewLayout

@Composable
fun FilterRow(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = contentPadding,
        content = content
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun FilterItemPreview(
    @PreviewParameter(FiltersParameter::class, 1)
    parameter: List<Filter>
) = PreviewLayout {
    FilterRow {
        items(parameter, Filter::value) {
            FilterItem(filter = it, onClick = {})
        }
    }
}

class FiltersParameter : CollectionPreviewParameterProvider<List<Filter>>(
    listOf(FilterParameter().values.toList())
)