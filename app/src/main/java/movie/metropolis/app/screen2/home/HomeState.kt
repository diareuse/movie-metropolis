package movie.metropolis.app.screen2.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import movie.metropolis.app.R

enum class HomeState(
    @DrawableRes
    val active: Int,
    @DrawableRes
    val inactive: Int,
    @StringRes
    val title: Int
) {
    Listing(
        active = R.drawable.ic_listing_active,
        inactive = R.drawable.ic_listing_inactive,
        title = R.string.movies
    ),
    Cinemas(
        active = R.drawable.ic_cinemas_active,
        inactive = R.drawable.ic_cinemas_inactive,
        title = R.string.cinemas
    ),
    Tickets(
        active = R.drawable.ic_ticket_active,
        inactive = R.drawable.ic_ticket_inactive,
        title = R.string.tickets
    ),
    Profile(
        active = R.drawable.ic_profile_active,
        inactive = R.drawable.ic_profile_inactive,
        title = R.string.user
    );

    companion object {
        fun by(route: String?) = entries.firstOrNull { it.name == route } ?: Listing
    }
}