@file:OptIn(ExperimentalLayoutApi::class)

package movie.metropolis.app.screen2.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.DataFiltersView
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.screen2.booking.component.FilterBox
import movie.metropolis.app.screen2.booking.component.ProjectionTypeRowDefaults
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
        Text("Filters", style = Theme.textStyle.title)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (it in filters.languages) FilterBox(
                selected = it.selected,
                onClick = { onLanguageClick(it) }
            ) {
                Text(it.locale.getDisplayLanguage(locale))
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (it in filters.types) FilterBox(
                selected = it.selected,
                onClick = { onTypeClick(it) }
            ) {
                when (val p = it.type) {
                    ProjectionType.Imax -> ProjectionTypeRowDefaults.TypeImax()
                    ProjectionType.Plane2D -> ProjectionTypeRowDefaults.Type2D()
                    ProjectionType.Plane3D -> ProjectionTypeRowDefaults.Type3D()
                    ProjectionType.Plane4DX -> ProjectionTypeRowDefaults.Type4DX()
                    ProjectionType.DolbyAtmos -> ProjectionTypeRowDefaults.DolbyAtmos()
                    ProjectionType.HighFrameRate -> ProjectionTypeRowDefaults.HighFrameRate()
                    ProjectionType.VIP -> ProjectionTypeRowDefaults.VIP()
                    is ProjectionType.Other -> ProjectionTypeRowDefaults.TypeOther(p.type)
                }
            }
        }
    }
}

@Preview
@Composable
private fun BookingFiltersDialogPreview() = PreviewLayout {
    BookingFiltersDialog(DataFiltersView(), {}, {})
}