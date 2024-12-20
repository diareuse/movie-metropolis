package movie.metropolis.app.ui.booking.component

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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FiltersColumn(
    filters: FiltersView,
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
                onClick = { it.selected = !it.selected }
            ) {
                Text(it.locale.getDisplayLanguage(locale))
            }
        }
        Text(stringResource(R.string.projection_types), style = Theme.textStyle.title)
        FlowRow {
            for (it in filters.types) FilterBox(
                selected = it.selected,
                onClick = { it.selected = !it.selected }
            ) {
                ProjectionTypeRow(type = it.type)
            }
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun FiltersColumnPreview() = PreviewLayout {
    val filters = remember {
        FiltersView().apply {
            this.types += listOf(
                FiltersView.Type(ProjectionType.Imax),
                FiltersView.Type(ProjectionType.Plane2D),
                FiltersView.Type(ProjectionType.Plane3D),
                FiltersView.Type(ProjectionType.Plane4DX),
                FiltersView.Type(ProjectionType.DolbyAtmos),
                FiltersView.Type(ProjectionType.HighFrameRate),
                FiltersView.Type(ProjectionType.VIP),
                FiltersView.Type(ProjectionType.Remaster)
            )
            this.languages += Locale.getISOLanguages().take(10)
                .map { FiltersView.Language(Locale.forLanguageTag(it)) }
        }
    }
    FiltersColumn(filters)
}