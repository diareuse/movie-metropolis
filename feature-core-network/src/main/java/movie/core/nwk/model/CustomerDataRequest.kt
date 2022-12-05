package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.core.nwk.serializer.LocaleSerializer
import java.util.Locale

@Serializable
data class CustomerDataRequest(
    @SerialName("consents") val consent: ConsentRemote,
    @SerialName("email") val email: String,
    @SerialName("favouriteCinemaId") val favoriteCinema: String?,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("phoneNumber") val phone: String,
    @Serializable(LocaleSerializer::class)
    @SerialName("locale") val locale: Locale = Locale.getDefault(),
    @SerialName("type") val type: String = "basic"
) {

    constructor(
        customer: CustomerResponse.Customer
    ) : this(
        customer.consent,
        customer.email,
        customer.favoriteCinema,
        customer.firstName,
        customer.lastName,
        customer.phone
    )

}