package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.R
import movie.metropolis.app.model.Filter
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterItem(
    filter: Filter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        modifier = modifier,
        selected = filter.isSelected,
        onClick = onClick,
        label = { Text(filter.value) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Theme.color.container.surface,
            labelColor = Theme.color.content.surface,
            iconColor = Theme.color.content.surface,
            selectedContainerColor = Theme.color.container.tertiary,
            selectedLabelColor = Theme.color.content.tertiary,
            selectedLeadingIconColor = Theme.color.content.tertiary,
            selectedTrailingIconColor = Theme.color.content.tertiary
        ),
        shape = CircleShape,
        border = null,
        leadingIcon = {
            AnimatedVisibility(
                visible = filter.isSelected,
                enter = expandIn(expandFrom = Alignment.CenterStart),
                exit = shrinkOut(shrinkTowards = Alignment.CenterStart)
            ) {
                Icon(painterResource(id = R.drawable.ic_done), null)
            }
        }
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun FilterItemPreview(
    @PreviewParameter(FilterParameter::class)
    parameter: Filter
) = PreviewLayout {
    FilterItem(parameter, {})
}

class FilterParameter : CollectionPreviewParameterProvider<Filter>(
    listOf(
        Filter(true, "IMAX"),
        Filter(false, "English")
    )
)