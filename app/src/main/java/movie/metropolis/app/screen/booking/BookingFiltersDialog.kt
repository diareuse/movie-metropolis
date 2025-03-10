@file:OptIn(ExperimentalLayoutApi::class)

package movie.metropolis.app.screen.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.screen.booking.component.FilterBox
import movie.metropolis.app.screen.booking.component.ProjectionTypeRow
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme
import java.util.Locale

@Composable
fun BookingFiltersDialog(
    filters: FiltersView,
    onLanguageClick: (FiltersView.Language) -> Unit,
    onTypeClick: (FiltersView.Type) -> Unit,
    modifier: Modifier = Modifier,
) {
    val locale = remember { Locale.getDefault() }
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.languages), style = Theme.textStyle.title)
        FlowRow {
            for (it in filters.languages) FilterBox(
                selected = it.selected,
                onClick = { onLanguageClick(it) }
            ) {
                Text(it.locale.getDisplayLanguage(locale))
            }
        }
        Text(stringResource(R.string.projection_types), style = Theme.textStyle.title)
        FlowRow {
            for (it in filters.types) FilterBox(
                selected = it.selected,
                onClick = { onTypeClick(it) }
            ) {
                ProjectionTypeRow(type = it.type)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookingFiltersDialogPreview() = PreviewLayout {
    val filters = FiltersView().apply {
        languages += listOf(
            FiltersView.Language(Locale.ENGLISH),
            FiltersView.Language(Locale.FRENCH).apply { selected = true }
        )
        types += listOf(
            FiltersView.Type(ProjectionType.Plane4DX),
            FiltersView.Type(ProjectionType.Plane3D).apply { selected = true }
        )
    }
    BookingFiltersDialog(filters, {}, {})
}