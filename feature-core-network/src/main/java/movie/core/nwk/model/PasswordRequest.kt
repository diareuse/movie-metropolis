package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PasswordRequest(
    @SerialName("oldPassword") val old: String,
    @SerialName("newPassword") val new: String
)