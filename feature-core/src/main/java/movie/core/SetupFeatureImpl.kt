package movie.core

import movie.core.model.Region
import movie.core.preference.RegionPreference

class SetupFeatureImpl(
    private val preference: RegionPreference
) : SetupFeature {

    override val requiresSetup: Boolean
        get() = preference.runCatching { "$domain$id" }.isFailure

    override var region: Region
        get() = preference.runCatching { domain to id }
            .map { (domain, id) -> Region.by(domain, id) }
            .getOrDefault(Region.Custom("https://localhost", 0))
        set(value) {
            preference.domain = value.domain
            preference.id = value.id
        }

}