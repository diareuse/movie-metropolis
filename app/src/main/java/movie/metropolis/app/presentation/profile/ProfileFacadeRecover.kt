package movie.metropolis.app.presentation.profile

import movie.core.Recoverable
import movie.core.ResultCallback
import movie.core.result
import movie.log.logSevere
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView

class ProfileFacadeRecover(
    private val origin: ProfileFacade
) : ProfileFacade, Recoverable {

    override suspend fun getCinemas(callback: ResultCallback<List<CinemaSimpleView>>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getCinemas(it)
        }
    }

    override suspend fun getMembership(callback: ResultCallback<MembershipView?>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getMembership(it)
        }
    }

    override suspend fun getUser(callback: ResultCallback<UserView>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getUser(it)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return origin.runCatching { isLoggedIn() }.logSevere().getOrDefault(false)
    }

    override suspend fun save(view: UserView) {
        origin.runCatching { save(view) }.logSevere()
    }

    override suspend fun save(passwordOld: String, passwordNew: String) {
        origin.runCatching { save(passwordOld, passwordNew) }.logSevere()
    }

}