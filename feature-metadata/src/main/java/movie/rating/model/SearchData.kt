package movie.rating.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.rating.DateSerializer
import java.util.Date

@Serializable
data class SearchData(
    @SerialName("id")
    val id: Long,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("vote_average")
    val rating: Double,
    @Serializable(with = DateSerializer::class)
    @SerialName("release_date")
    val releaseDate: Date?,
    @SerialName("overview")
    val description: String
)