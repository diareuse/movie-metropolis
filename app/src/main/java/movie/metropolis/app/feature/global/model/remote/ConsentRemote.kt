package movie.metropolis.app.feature.global.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConsentRemote(
    @SerialName("marketingConsent") val marketing: Boolean,
    @SerialName("premiumMarketingConsent") val marketingPremium: Boolean? = null
)