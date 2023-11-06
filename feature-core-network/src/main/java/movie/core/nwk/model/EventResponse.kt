package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.core.nwk.serializer.LocalTimestampSerializer
import java.util.Date
import java.util.Locale

@Serializable
data class EventResponse(
    @SerialName("id") val id: String,
    @SerialName("filmId") val movieId: String,
    @SerialName("cinemaId") val cinemaId: String,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("eventDateTime") val startsAt: Date,
    @SerialName("bookingLink") val bookingUrl: String,
    @SerialName("attributeIds") val tags: List<String>,
    @SerialName("soldOut") val soldOut: Boolean,
    @SerialName("auditorium") val auditorium: String
) {

    val dubbing: Locale
        get() = when {
            DubbedTag in tags -> tags.firstAround(Dubbing, "-").orEmpty()
                .let(::Locale)

            else -> Locale.ROOT
        }

    val subtitles: Locale?
        get() = when {
            SubbedTag in tags -> tags.firstAround(Subbed, "-").orEmpty()
                .let(::Locale)

            else -> null
        }

    val types
        get() = buildList {
            if (ScreeningType2D in tags) add("2D")
            if (ScreeningType3D in tags) add("3D")
            if (ScreeningTypeDolbyAtmos in tags) add("Dolby Atmos")
            if (ScreeningType4DX in tags) add("4DX")
            if (ScreeningTypeIMAX in tags) add("IMAX")
            if (ScreeningTypeVIP in tags) add("VIP")
            if (ScreeningTypeRemaster in tags) add("Remaster")
            if (ScreeningTypeHighFrameRate in tags) add("High Frame Rate")
        }

    private fun List<String>.firstAround(start: String, end: String) =
        firstOrNull { it.startsWith(start) }
            ?.around(start, end)

    private fun String.around(start: String, end: String) = substringAfter(start)
        .substringBefore(end)

    companion object {
        const val SubbedTag = "subbed"
        const val DubbedTag = "dubbed"
        const val Subbed = "first-subbed-lang-"
        const val Dubbing = "dubbed-lang-"
        const val Original = "original-lang-"
        const val ScreeningType2D = "2d"
        const val ScreeningType3D = "3d"
        const val ScreeningTypeDolbyAtmos = "dolby-atmos"
        const val ScreeningType4DX = "4dx"
        const val ScreeningTypeIMAX = "imax"
        const val ScreeningTypeVIP = "vip"
        const val ScreeningTypeRemaster = "dmr"
        const val ScreeningTypeHighFrameRate = "hfr"
    }

}