package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.Filter
import movie.style.AppButton
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun FilterItem(
    filter: Filter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = when (filter.isSelected) {
            true -> Theme.color.container.tertiary
            else -> Theme.color.container.surface
        },
        contentColor = when (filter.isSelected) {
            true -> Theme.color.content.tertiary
            else -> Theme.color.content.surface
        },
        contentPadding = PaddingValues(16.dp, 8.dp),
        elevation = 0.dp
    ) {
        val style = Theme.textStyle.body
        if (filter.isSelected) {
            Icon(
                modifier = Modifier.size(with(LocalDensity.current) { style.fontSize.toDp() }),
                painter = painterResource(R.drawable.ic_selected),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = filter.value,
            style = style
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun FilterItemPreview(
    @PreviewParameter(FilterParameter::class, 1)
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