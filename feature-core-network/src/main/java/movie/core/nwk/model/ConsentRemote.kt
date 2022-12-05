package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsentRemote(
    @SerialName("marketingConsent") val marketing: Boolean,
    @SerialName("premiumMarketingConsent") val marketingPremium: Boolean? = null
)