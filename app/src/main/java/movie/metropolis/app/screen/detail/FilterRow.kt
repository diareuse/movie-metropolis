package movie.metropolis.app.screen.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.metropolis.app.model.Filter
import movie.style.haptic.withHaptics
import movie.style.theme.Theme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterRow(
    filters: List<Filter>,
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
    Surface(
        modifier = modifier,
        color = when (selected) {
            true -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.tertiaryContainer
        },
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick.withHaptics())
                .padding(16.dp, 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val style = MaterialTheme.typography.bodyMedium
            if (selected)
                Icon(
                    modifier = Modifier.size(with(LocalDensity.current) { style.fontSize.toDp() }),
                    painter = painterResource(R.drawable.ic_selected),
                    contentDescription = null
                )
            Text(
                text = name,
                style = style,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        val filters = listOf(Filter(true, "IMAX"), Filter(false, "English"))
        FilterRow(
            filters = filters,
            onFilterToggle = {}
        )
    }
}