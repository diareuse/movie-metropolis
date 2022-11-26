package movie.metropolis.app.feature.global

import java.util.Date

interface Showing {
    val id: String
    val cinema: Cinema
    val startsAt: Date
    val bookingUrl: String // add "access-token" and current token as header to webview when opening
    val isEnabled: Boolean // ie. is not sold out
    val auditorium: String
    val language: String
    val type: String
}