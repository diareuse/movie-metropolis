package movie.metropolis.app.model.adapter

import android.net.Uri
import movie.core.model.Cinema
import movie.metropolis.app.model.CinemaView

data class CinemaViewFromFeature(
    private val cinema: Cinema
) : CinemaView {
    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name.substringAfterLast(',').trim()
    override val address: String
        get() = cinema.address.filter { it !in name }.joinToString("\n")
    override val city: String
        get() = cinema.city
    override val distance: String?
        get() = cinema.distance?.let {
            when {
                it < 1.0 -> "%.2f km".format(it)
                it < 10.0 -> "%.1f km".format(it)
                else -> "%.0f km".format(it)
            }
        }
    override val image: String?
        get() = cinema.image
    override val uri: String
        get() = "geo:${cinema.location.latitude},${cinema.location.longitude}?q=" + Uri.encode(name)
}