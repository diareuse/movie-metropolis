package movie.metropolis.app.model.adapter

import movie.cinema.city.Occurrence
import movie.metropolis.app.model.AvailabilityView

data class LanguageAndTypeFromFeature(
    private val item: Occurrence
) : AvailabilityView.Type {

    override val language get() = item.dubbing.displayLanguage
    override val types get() = item.flags.map { it.tag.uppercase() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LanguageAndTypeFromFeature) return false

        if (language != other.language) return false
        if (types != other.types) return false

        return true
    }

    override fun hashCode(): Int {
        var result = language.hashCode()
        result = 31 * result + types.hashCode()
        return result
    }

}