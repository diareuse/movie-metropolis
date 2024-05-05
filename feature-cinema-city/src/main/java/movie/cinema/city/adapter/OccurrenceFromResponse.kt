package movie.cinema.city.adapter

import movie.cinema.city.Cinema
import movie.cinema.city.Occurrence
import movie.cinema.city.model.EventResponse
import java.net.URL
import java.util.Date
import java.util.Locale

internal data class OccurrenceFromResponse(
    private val response: EventResponse,
    override val cinema: Cinema
) : Occurrence {
    override val id: String
        get() = response.id
    override val booking: URL
        get() = URL(response.bookingUrl)
    override val startsAt: Date
        get() = response.startsAt
    override val flags: Set<Occurrence.Flag>
        get() = Occurrence.Flag.entries.filter { it.tag in response.tags }.toSet()
    override val dubbing: Locale
        get() = response.dubbing
    override val subtitles: Locale?
        get() = response.subtitles
}