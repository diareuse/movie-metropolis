package movie.metropolis.app.model.adapter

import movie.core.model.Showing
import movie.metropolis.app.model.AvailabilityView

class LanguageAndTypeFromFeature(
    private val item: Showing
) : AvailabilityView.Type {
    override val language: String get() = item.language
    override val type: String get() = item.type

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LanguageAndTypeFromFeature) return false

        if (language != other.language) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = language.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

}