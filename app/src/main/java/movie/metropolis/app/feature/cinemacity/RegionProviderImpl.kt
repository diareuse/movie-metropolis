package movie.metropolis.app.feature.cinemacity

import movie.cinema.city.Region
import movie.cinema.city.RegionProvider
import movie.settings.GlobalPreferences

class RegionProviderImpl(
    private val prefs: GlobalPreferences
) : RegionProvider {
    override val region: Region?
        get() = prefs.regionId?.let(Region.Companion::by)

    override fun setRegion(region: Region) {
        prefs.regionId = region.id
    }
}