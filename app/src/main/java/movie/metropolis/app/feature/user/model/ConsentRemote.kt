package movie.metropolis.app.feature.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConsentRemote(
    @SerialName("marketingConsent") val marketing: Boolean,
    @SerialName("premiumMarketingConsent") val marketingPremium: Boolean
)