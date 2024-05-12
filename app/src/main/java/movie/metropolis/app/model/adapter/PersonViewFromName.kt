package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.PersonView

data class PersonViewFromName(
    override val name: String
) : PersonView {
    override val url: String = ""
    override val popularity: Int = 0
    override val image: String = ""
    override val starredInMovies: Int = 0
}