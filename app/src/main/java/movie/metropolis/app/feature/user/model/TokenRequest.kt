package movie.metropolis.app.feature.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed class TokenRequest {

    @Serializable
    data class Login(
        @SerialName("username") val username: String,
        @SerialName("password") val password: String,
        @SerialName("reCaptcha") val captcha: String,
        @SerialName("grant_type") val grantType: String = "password"
    ) : TokenRequest()

    @Serializable
    data class Refresh(
        @SerialName("refresh_token") val token: String,
        @SerialName("reCaptcha") val captcha: String,
        @SerialName("grant_type") val grantType: String = "refresh_token"
    ) : TokenRequest()

}