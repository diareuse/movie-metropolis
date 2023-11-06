package movie.core.adapter

import movie.core.model.Cinema
import movie.core.model.Showing
import movie.core.nwk.model.EventResponse
import java.util.Date
import java.util.Locale

internal data class ShowingFromResponse(
    private val showing: EventResponse,
    override val cinema: Cinema
) : Showing {

    override val id: String
        get() = showing.id
    override val startsAt: Date
        get() = showing.startsAt
    override val bookingUrl: String
        get() = showing.bookingUrl
    override val isEnabled: Boolean
        get() = !showing.soldOut && Date().before(startsAt)
    override val auditorium: String
        get() = showing.auditorium
    override val language: Locale
        get() = showing.dubbing
    override val subtitles: Locale?
        get() = showing.subtitles
    override val types: Iterable<String>
        get() = showing.types

}