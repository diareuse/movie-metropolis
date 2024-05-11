package movie.cinema.city

interface RegionProvider {
    val region: Region?
    fun setRegion(region: Region)
}

fun RegionProvider.requireRegion() = requireNotNull(region)

internal val RegionProvider.domain
    get() = requireRegion().domain
internal val RegionProvider.id
    get() = requireRegion().id
internal val RegionProvider.tld
    get() = requireRegion().domain.substringAfterLast('.').substringBefore('/')