package movie.core.adapter

import movie.core.model.Cinema
import movie.core.model.Showing
import java.util.Date

internal data class ShowingFromResponse(
    private val showing: movie.core.nwk.model.EventResponse,
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
    override val language: String
        get() = showing.language
    override val type: String
        get() = showing.type

}