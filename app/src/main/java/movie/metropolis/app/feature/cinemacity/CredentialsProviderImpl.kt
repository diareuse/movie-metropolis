package movie.metropolis.app.feature.cinemacity

import movie.cinema.city.Credentials
import movie.cinema.city.CredentialsProvider
import movie.cinema.city.Region
import movie.cinema.city.RegionProvider
import movie.core.auth.UserAccount
import movie.settings.GlobalPreferences

class CredentialsProviderImpl(
    private val user: UserAccount
) : CredentialsProvider {
    override suspend fun get() = Credentials(
        username = user.email.let(::requireNotNull),
        password = user.password.let(::requireNotNull)
    )
}

class RegionProviderImpl(
    private val prefs: GlobalPreferences
) : RegionProvider {
    override val region: Region?
        get() = prefs.regionId?.let(Region.Companion::by)

    override fun setRegion(region: Region) {
        prefs.regionId = region.id
    }
}