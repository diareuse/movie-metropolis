package movie.metropolis.app.feature.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.metropolis.app.feature.user.serializer.LocaleSerializer
import java.util.Locale

@Serializable
internal data class RegistrationRequest(
    @SerialName("email") val email: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @Serializable(LocaleSerializer::class)
    @SerialName("locale") val locale: Locale = Locale.getDefault(),
    @SerialName("password") val password: String,
    @SerialName("phoneNumber") val phone: String,
    @SerialName("consents") val consent: ConsentRemote = ConsentRemote(false),
    @SerialName("terms") val terms: Terms = Terms(),
    @SerialName("type") val type: String = "basic"
) {

    @Serializable
    data class Terms(
        // smh, this requirement is just retarded
        @SerialName("termsAccepted") val accepted: Boolean = true
    )

}