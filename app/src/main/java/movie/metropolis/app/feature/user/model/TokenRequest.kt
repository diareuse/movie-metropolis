package movie.metropolis.app.feature.user.model

import io.ktor.http.Parameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.metropolis.app.BuildConfig

internal sealed class TokenRequest {

    abstract fun toParameters(): Parameters

    @Serializable
    data class Login(
        @SerialName("username") val username: String,
        @SerialName("password") val password: String,
        @SerialName("reCaptcha") val captcha: String = BuildConfig.Captcha,
        @SerialName("grant_type") val grantType: String = "password"
    ) : TokenRequest() {

        override fun toParameters() = Parameters.build {
            append("username", username)
            append("password", password)
            append("reCaptcha", captcha)
            append("grant_type", grantType)
        }

    }

    @Serializable
    data class Refresh(
        @SerialName("refresh_token") val token: String,
        @SerialName("reCaptcha") val captcha: String = BuildConfig.Captcha,
        @SerialName("grant_type") val grantType: String = "refresh_token"
    ) : TokenRequest() {

        override fun toParameters() = Parameters.build {
            append("refresh_token", token)
            append("reCaptcha", captcha)
            append("grant_type", grantType)
        }

    }

}