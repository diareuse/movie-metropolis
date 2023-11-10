package movie.metropolis.app.model

import androidx.compose.runtime.*
import kotlinx.collections.immutable.ImmutableList
import java.util.Locale

@Immutable
data class ShowingTag(
    val language: Locale,
    val subtitles: Locale?,
    val projection: ImmutableList<ProjectionType>
)