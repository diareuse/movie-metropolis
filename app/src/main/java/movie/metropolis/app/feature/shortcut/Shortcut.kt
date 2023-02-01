package movie.metropolis.app.feature.shortcut

import android.content.Context
import movie.metropolis.app.R
import movie.metropolis.app.model.CinemaView

fun Context.createShortcut(cinema: CinemaView) = ShortcutFactory.unique(this)
    .setIcon(R.drawable.icon_cinema)
    .setLabel(cinema.name.substringBefore(","))
    .setId("cinema")
    .setRoute("cinemas/${cinema.id}")
    .create()