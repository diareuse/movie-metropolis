package movie.metropolis.app.screen.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.presentation.Loadable
import movie.style.EllipsisText
import movie.style.textPlaceholder
import movie.style.theme.Theme

@Composable
fun MovieDetailColumn(
    detail: Loadable<MovieDetailView>,
    modifier: Modifier = Modifier,
) {
    val detailView = detail.getOrNull()
    Column(modifier = modifier) {
        Text(
            text = detailView?.name ?: "#".repeat(10),
            style = Theme.textStyle.title,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "%s • %s • %s".format(
                detailView?.duration ?: "#".repeat(6),
                detailView?.countryOfOrigin ?: "#".repeat(3),
                detailView?.releasedAt ?: "#".repeat(4)
            ),
            style = Theme.textStyle.caption,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = detailView?.directors?.joinToString() ?: "#".repeat(13),
            style = Theme.textStyle.caption,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        EllipsisText(
            text = detailView?.cast?.joinToString() ?: "#".repeat(28),
            maxLines = 3,
            style = Theme.textStyle.caption,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
    }
}