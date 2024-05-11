package movie.metropolis.app.model.adapter

import android.net.Uri
import movie.cinema.city.Cinema
import movie.metropolis.app.model.CinemaView

data class CinemaViewFromFeature(
    private val cinema: Cinema
) : CinemaView {
    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name.substringAfterLast(',').trim()
    override val address: String
        get() = cinema.address.address.filter { it !in name }.joinToString("\n")
    override val city: String
        get() = cinema.address.city
    override val distance: String?
        get() = null
    override val image: String
        get() = cinema.image.toString()
    override val uri: String
        get() = "geo:${cinema.address.latitude},${cinema.address.longitude}?q=" + Uri.encode(name)
}