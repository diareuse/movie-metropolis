package movie.metropolis.app.feature.global.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.metropolis.app.feature.global.serializer.DateSerializer
import movie.metropolis.app.feature.global.serializer.LocalTimestampSerializer
import java.util.Date

@Serializable
internal data class CustomerResponse(
    @SerialName("customer") val customer: Customer,
    @SerialName("accessToken") val token: TokenResponse?
) {

    @Serializable
    data class Customer(
        @SerialName("id") val id: String,
        @SerialName("firstName") val firstName: String,
        @SerialName("lastName") val lastName: String,
        @SerialName("email") val email: String,
        @SerialName("phoneNumber") val phone: String,
        @Serializable(DateSerializer::class)
        @SerialName("birthDate") val birthAt: Date?,
        @SerialName("favouriteCinemaId") val favoriteCinema: String?,
        @SerialName("consents") val consent: ConsentRemote,
        @SerialName("memberships") val membership: Membership
    )

    @Serializable
    data class Membership(
        @SerialName("eClub") val club: Club?,
        @SerialName("basic") val basic: Map<String, @Contextual Any?>
    )

    @Serializable
    data class Club(
        @SerialName("cardNumber") val cardNumber: String,
        @SerialName("signupOptionId") val optionId: Long,
        @Serializable(LocalTimestampSerializer::class)
        @SerialName("joinDate") val joinedAt: Date,
        @Serializable(LocalTimestampSerializer::class)
        @SerialName("expirationDate") val expiredAt: Date,
        @SerialName("status") val status: MembershipState
    )

    @Serializable
    enum class MembershipState {
        @SerialName("EXPIRED")
        Expired,

        @SerialName("ACTIVE")
        Active
    }

}