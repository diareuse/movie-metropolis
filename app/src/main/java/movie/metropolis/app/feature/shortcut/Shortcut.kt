package movie.metropolis.app.feature.shortcut

import android.content.Context
import movie.metropolis.app.R
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.Route

fun Context.createShortcut(cinema: CinemaView) = ShortcutFactory.unique(this)
    .setIcon(R.drawable.icon_cinema)
    .setLabel(cinema.name.substringBefore(","))
    .setId("cinema")
    .setRoute(Route.Booking.Cinema.deepLink(cinema.id))
    .create()