package movie.metropolis.app.screen.profile

import movie.log.flatMapCatching
import movie.log.logSevere
import movie.metropolis.app.model.UserView

class ProfileFacadeRecover(
    private val origin: ProfileFacade
) : ProfileFacade {

    override suspend fun getCinemas() =
        origin.flatMapCatching { getCinemas() }.logSevere()

    override suspend fun getMembership() =
        origin.flatMapCatching { getMembership() }.logSevere()

    override suspend fun getUser() =
        origin.flatMapCatching { getUser() }.logSevere()

    override suspend fun isLoggedIn() =
        origin.runCatching { isLoggedIn() }.logSevere().getOrDefault(false)

    override suspend fun save(view: UserView) =
        origin.flatMapCatching { save(view) }.logSevere()

    override suspend fun save(passwordOld: String, passwordNew: String) =
        origin.flatMapCatching { save(passwordOld, passwordNew) }.logSevere()

}