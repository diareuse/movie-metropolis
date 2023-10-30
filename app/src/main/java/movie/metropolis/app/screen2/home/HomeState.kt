package movie.metropolis.app.screen2.home

import androidx.annotation.DrawableRes
import movie.metropolis.app.R

enum class HomeState(
    @DrawableRes
    val active: Int,
    @DrawableRes
    val inactive: Int,
) {
    Listing(
        active = R.drawable.ic_listing_active,
        inactive = R.drawable.ic_listing_inactive
    ),
    Tickets(
        active = R.drawable.ic_ticket_active,
        inactive = R.drawable.ic_ticket_inactive
    ),
    Cinemas(
        active = R.drawable.ic_cinemas_active,
        inactive = R.drawable.ic_cinemas_inactive
    ),
    Profile(
        active = R.drawable.ic_profile_active,
        inactive = R.drawable.ic_profile_inactive
    )
}