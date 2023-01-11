package movie.core.preference

interface RegionPreference {

    @get:Throws(IllegalArgumentException::class)
    var domain: String

    @get:Throws(IllegalArgumentException::class)
    var id: Int

}