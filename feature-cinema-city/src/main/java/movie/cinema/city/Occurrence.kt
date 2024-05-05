package movie.cinema.city

import java.net.URL
import java.util.Date
import java.util.Locale

interface Occurrence {
    val id: String
    val cinema: Cinema
    val booking: URL
    val startsAt: Date
    val flags: Set<Flag>
    val dubbing: Locale
    val subtitles: Locale?

    enum class Flag(val tag: String) {
        Projection2D("2d"),
        Projection3D("3d"),
        Projection4DX("4dx"),
        DolbyAtmos("dolby-atmos"),
        IMAX("imax"),
        VIP("vip"),
        Remaster("dmr"),
        HighFrameRate("hfr")
    }
}