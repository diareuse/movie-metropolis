package movie.metropolis.app.screen.profile

import movie.log.logCatching
import movie.log.logCatchingResult
import movie.metropolis.app.model.UserView

class ProfileFacadeRecover(
    private val origin: ProfileFacade
) : ProfileFacade {

    override suspend fun getCinemas() =
        origin.logCatchingResult("profile-cinemas") { getCinemas() }

    override suspend fun getMembership() =
        origin.logCatchingResult("profile-membership") { getMembership() }

    override suspend fun getUser() =
        origin.logCatchingResult("profile-user") { getUser() }

    override suspend fun isLoggedIn() =
        origin.logCatching("profile-logged-in") { isLoggedIn() }.getOrDefault(false)

    override suspend fun save(view: UserView) =
        origin.logCatchingResult("profile-save") { save(view) }

    override suspend fun save(passwordOld: String, passwordNew: String) =
        origin.logCatchingResult("profile-save-pw") { save(passwordOld, passwordNew) }

}