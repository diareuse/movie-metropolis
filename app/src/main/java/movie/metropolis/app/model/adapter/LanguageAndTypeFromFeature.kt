package movie.metropolis.app.model.adapter

import movie.core.model.Showing
import movie.metropolis.app.model.AvailabilityView

data class LanguageAndTypeFromFeature(
    private val item: Showing
) : AvailabilityView.Type {

    override val language get() = item.language.displayLanguage
    override val types get() = item.types.toList()

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