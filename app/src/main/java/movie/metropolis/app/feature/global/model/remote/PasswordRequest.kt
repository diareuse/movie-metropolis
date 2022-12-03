package movie.metropolis.app.feature.global.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PasswordRequest(
    @SerialName("oldPassword") val old: String,
    @SerialName("newPassword") val new: String
)