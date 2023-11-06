package movie.metropolis.app.model

import java.util.Locale

data class ShowingTag(
    val language: Locale,
    val subtitles: Locale?,
    val projection: List<ProjectionType>
)