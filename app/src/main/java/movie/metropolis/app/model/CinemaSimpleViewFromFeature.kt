package movie.metropolis.app.model

import movie.metropolis.app.feature.global.Cinema

data class CinemaSimpleViewFromFeature(
    private val cinema: Cinema
) : CinemaSimpleView {

    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name
    override val city: String
        get() = cinema.city

}