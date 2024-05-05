package movie.cinema.city.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.cinema.city.serializer.DateSerializer
import movie.cinema.city.serializer.LocalTimestampSerializer
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
        @SerialName("eClub") val club: Club?
    )

    @Serializable
    data class Club(
        @SerialName("cardNumber") val cardNumber: String,
        @Serializable(LocalTimestampSerializer::class)
        @SerialName("joinDate") val joinedAt: Date,
        @Serializable(LocalTimestampSerializer::class)
        @SerialName("expirationDate") val expiredAt: Date
    )

}