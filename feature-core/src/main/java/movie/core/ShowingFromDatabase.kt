package movie.core

import movie.core.db.model.ShowingStored
import movie.core.model.Cinema
import movie.core.model.Showing
import java.util.Date

data class ShowingFromDatabase(
    private val showing: ShowingStored,
    override val cinema: Cinema
) : Showing {
    override val id: String
        get() = showing.id
    override val startsAt: Date
        get() = showing.startsAt
    override val bookingUrl: String
        get() = showing.bookingUrl
    override val isEnabled: Boolean
        get() = showing.isEnabled
    override val auditorium: String
        get() = showing.auditorium
    override val language: String
        get() = showing.language
    override val types: Iterable<String>
        get() = showing.types
}