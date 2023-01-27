package movie.metropolis.app.screen.detail.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.metropolis.app.model.Filter
import movie.style.AppButton
import movie.style.state.ImmutableList
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.theme.Theme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterRow(
    filters: ImmutableList<Filter>,
    onFilterToggle: (Filter) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = contentPadding
    ) {
        items(filters, key = Filter::value) {
            FilterItem(
                modifier = Modifier.animateItemPlacement(),
                name = it.value,
                selected = it.isSelected,
                onClick = { onFilterToggle(it) }
            )
        }
    }
}

@Composable
private fun FilterItem(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = when (selected) {
            true -> Theme.color.container.tertiary
            else -> Theme.color.container.surface
        },
        contentColor = when (selected) {
            true -> Theme.color.content.tertiary
            else -> Theme.color.content.surface
        },
        contentPadding = PaddingValues(16.dp, 8.dp),
        elevation = 0.dp
    ) {
        val style = Theme.textStyle.body
        if (selected) {
            Icon(
                modifier = Modifier.size(with(LocalDensity.current) { style.fontSize.toDp() }),
                painter = painterResource(R.drawable.ic_selected),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = name,
            style = style
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        val filters = listOf(Filter(true, "IMAX"), Filter(false, "English")).immutable()
        FilterRow(
            filters = filters,
            onFilterToggle = {}
        )
    }
}