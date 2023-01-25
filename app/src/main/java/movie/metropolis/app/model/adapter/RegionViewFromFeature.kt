package movie.metropolis.app.model.adapter

import movie.core.model.Region
import movie.metropolis.app.model.RegionView
import java.util.Locale

data class RegionViewFromFeature(
    val region: Region,
    private val locale: Locale
) : RegionView {

    override val name: String
        get() = locale.getDisplayCountry(locale)

}