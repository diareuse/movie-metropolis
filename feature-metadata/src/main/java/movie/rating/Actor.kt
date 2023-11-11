package movie.rating

import movie.rating.model.ActorReference

data class Actor(
    val id: Long,
    val name: String,
    val popularity: Int,
    val image: String,
    val movies: List<ActorReference>
)