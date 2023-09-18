package movie.rating

sealed class MovieDescriptor(name: String) {

    abstract val year: Int
    val name = name
        .replace(Regex("\\d[Dd][Xx]?(\\s*-\\s*(sub|dub))?"), "")
        .replace("the", "", true)
        .replace("  ", " ")
        .trim()

    class Original(name: String, override val year: Int) : MovieDescriptor(name)
    class Local(name: String, override val year: Int) : MovieDescriptor(name)

    operator fun component1() = name
    operator fun component2() = year

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MovieDescriptor) return false

        if (name != other.name) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + year
        return result
    }

}