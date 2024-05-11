package movie.metropolis.app.presentation.setup

import movie.cinema.city.Region
import movie.cinema.city.RegionProvider
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.model.adapter.RegionViewFromFeature
import java.util.Locale

class SetupFacadeFromFeature(
    private val feature: RegionProvider
) : SetupFacade {

    override val requiresSetup: Boolean
        get() = feature.region == null

    override suspend fun getRegions() = buildList {
        this += RegionViewFromFeature(Region.Czechia, Locale("cs", "CZ"))
        this += RegionViewFromFeature(Region.Slovakia, Locale("sk", "SK"))
        this += RegionViewFromFeature(Region.Poland, Locale("pl", "PL"))
        this += RegionViewFromFeature(Region.Hungary, Locale("hu", "HU"))
        this += RegionViewFromFeature(Region.Romania, Locale("ro", "RO"))
    }.let(Result.Companion::success)

    override suspend fun select(view: RegionView) {
        require(view is RegionViewFromFeature)
        feature.setRegion(view.region)
    }
}