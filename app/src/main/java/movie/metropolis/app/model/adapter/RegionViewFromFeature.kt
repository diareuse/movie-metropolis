package movie.metropolis.app.model.adapter

import movie.core.model.Region
import movie.metropolis.app.R
import movie.metropolis.app.model.RegionView
import java.util.Locale

data class RegionViewFromFeature(
    val region: Region,
    private val locale: Locale
) : RegionView {

    override val name: String
        get() = locale.getDisplayCountry(locale)

    override val icon: Int
        get() = when (locale.country.lowercase()) {
            "cz" -> R.drawable.ic_czechia
            "sk" -> R.drawable.ic_slovakia
            "hu" -> R.drawable.ic_hungary
            "ro" -> R.drawable.ic_romania
            "pl" -> R.drawable.ic_poland
            else -> R.drawable.ic_eu
        }

}