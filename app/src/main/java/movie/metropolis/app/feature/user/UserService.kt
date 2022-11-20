package movie.metropolis.app.feature.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal interface UserService {

    suspend fun getToken(request: TokenRequest): Result<TokenResponse>
    suspend fun getToken(refreshKey: String): Result<TokenResponse>

    suspend fun updateUser(data: CustomerDataRequest): Result<CustomerResponse>
    suspend fun getPoints(): Result<CustomerPointsResponse>
    suspend fun getUser(): Result<CustomerResponse.Customer>
    suspend fun getBookings(): Result<List<BookingResponse>>

}

@Serializable
internal data class BookingResponse(
    @SerialName("id") val id: String,
    @SerialName("eventName") val name: String,
    @Serializable(TimestampSerializer::class)
    @SerialName("eventDate") val startsAt: Date,
    @Serializable(TimestampSerializer::class)
    @SerialName("transactionDate") val paidAt: Date,
    @SerialName("isPaid") val isPaid: Boolean,
    @SerialName("eventMasterCode") val eventMasterCode: String,
    @SerialName("cinemaId") val cinemaId: CinemaId,
    @SerialName("eventId") val eventId: String
)

@Serializable
internal data class CustomerPointsResponse(
    @SerialName("totalPoints") val total: Double,
    @SerialName("pointsToExpire") val expire: Double,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("expirationDate") val expiresAt: Date
)

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

@Serializable
internal data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val type: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("expires_in") val expiresIn: Long
)

internal typealias CinemaId = String

@Serializable
internal data class CustomerDataRequest(
    @SerialName("consents") val consent: ConsentRemote,
    @SerialName("email") val email: String,
    @SerialName("favouriteCinemaId") val favoriteCinema: CinemaId,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("phoneNumber") val phone: String,
    @Serializable(LocaleSerializer::class)
    @SerialName("locale") val locale: Locale = Locale.getDefault(),
    @SerialName("type") val type: String = "basic"
)

@Serializable
internal data class ConsentRemote(
    @SerialName("marketingConsent") val marketing: Boolean,
    @SerialName("premiumMarketingConsent") val marketingPremium: Boolean
)

@Serializable
internal data class CustomerResponse(
    @SerialName("customer") val customer: Customer
) {

    @Serializable
    data class Customer(
        @SerialName("id") val id: String,
        @SerialName("firstName") val firstName: String,
        @SerialName("lastName") val lastName: String,
        @SerialName("email") val email: String,
        @SerialName("phoneNumber") val phone: String,
        @Serializable(DateSerializer::class)
        @SerialName("birthDate") val birthAt: Date,
        @SerialName("favouriteCinemaId") val favoriteCinema: CinemaId,
        @SerialName("consents") val consent: ConsentRemote,
        @SerialName("memberships") val membership: Membership
    )

    @Serializable
    data class Membership(
        @SerialName("eClub") val club: Club,
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

internal abstract class KDateSerializer : KSerializer<Date> {

    final override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(type, PrimitiveKind.STRING)

    abstract val type: String
    abstract val formatter: SimpleDateFormat

    final override fun deserialize(decoder: Decoder): Date {
        return formatter.parse(decoder.decodeString()).let(::requireNotNull)
    }

    final override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(formatter.format(value))
    }

}

internal class TimestampSerializer : KDateSerializer() {

    override val type: String = "timestamp+timezone"
    override val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

}

internal class LocalTimestampSerializer : KDateSerializer() {

    override val type = "timestamp"
    override val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("Europe/Prague")
    }

}

internal class DateSerializer : KDateSerializer() {

    override val type = "date"
    override val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

}

internal class LocaleSerializer : KSerializer<Locale> {

    override val descriptor = PrimitiveSerialDescriptor("locale", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Locale {
        return Locale(decoder.decodeString().replace("_", "-"))
    }

    override fun serialize(encoder: Encoder, value: Locale) {
        encoder.encodeString(value.toLanguageTag().replace("-", "_"))
    }

}
