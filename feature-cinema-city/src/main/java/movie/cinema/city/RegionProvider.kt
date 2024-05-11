package movie.cinema.city

interface RegionProvider {
    val region: Region?
    fun setRegion(region: Region)
}

fun RegionProvider.requireRegion() = requireNotNull(region)

val RegionProvider.domain
    get() = requireRegion().domain
val RegionProvider.id
    get() = requireRegion().id
val RegionProvider.tld
    get() = requireRegion().domain.substringAfterLast('.').substringBefore('/')