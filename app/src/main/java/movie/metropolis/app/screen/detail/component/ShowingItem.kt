package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.screen.cinema.component.ShowingTypeLayout
import movie.metropolis.app.screen.cinema.component.ShowingsLayout
import movie.metropolis.app.screen.detail.CinemaBookingViewProvider
import movie.style.layout.PreviewLayout

@Composable
fun ShowingItem(
    view: CinemaBookingView,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ShowingsLayout(
        modifier = modifier,
        title = { Text(view.cinema.name) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for ((type, availability) in view.availability) {
                ShowingTypeLayout(
                    type = {
                        Icon(
                            painterResource(R.drawable.ic_screening_type),
                            null,
                            Modifier.size(16.dp)
                        )
                        Text(type.type)
                    },
                    language = {
                        Icon(painterResource(R.drawable.ic_language), null, Modifier.size(16.dp))
                        Text(type.language)
                    }
                ) {
                    items(availability, AvailabilityView::id) {
                        ShowingItemTime(
                            onClick = { onClick(it.url) }
                        ) {
                            Text(it.startsAt)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ShowingItemPreview(
    @PreviewParameter(CinemaBookingViewProvider::class)
    item: CinemaBookingView
) = PreviewLayout {
    ShowingItem(item, {})
}