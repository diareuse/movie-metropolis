package movie.rating.model

import java.util.Date

data class ActorReference(
    val id: Long,
    val name: String,
    val backdrop: String,
    val image: String,
    val popularity: Int,
    val rating: Byte,
    val releasedAt: Date
)