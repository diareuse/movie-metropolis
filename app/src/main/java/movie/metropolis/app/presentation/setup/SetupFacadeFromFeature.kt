package movie.metropolis.app.presentation.setup

import movie.core.SetupFeature
import movie.core.model.Region
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.model.adapter.RegionViewFromFeature
import java.util.Locale

class SetupFacadeFromFeature(
    private val feature: SetupFeature
) : SetupFacade {

    override val requiresSetup: Boolean
        get() = feature.requiresSetup

    override suspend fun getRegions() = buildList {
        this += RegionViewFromFeature(Region.Czechia, Locale("cs", "CZ"))
        this += RegionViewFromFeature(Region.Slovakia, Locale("sk", "SK"))
        this += RegionViewFromFeature(Region.Poland, Locale("pl", "PL"))
        this += RegionViewFromFeature(Region.Hungary, Locale("hu", "HU"))
        this += RegionViewFromFeature(Region.Romania, Locale("ro", "RO"))
    }.let(Result.Companion::success)

    override suspend fun select(view: RegionView) {
        if (view !is RegionViewFromFeature) return
        feature.region = view.region
    }
}