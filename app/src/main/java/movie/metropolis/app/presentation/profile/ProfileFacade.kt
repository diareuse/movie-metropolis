package movie.metropolis.app.presentation.profile

import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView

interface ProfileFacade {

    suspend fun getCinemas(): List<CinemaSimpleView>
    suspend fun getMembership(): MembershipView?
    suspend fun getUser(): UserView?
    suspend fun isLoggedIn(): Boolean

    suspend fun save(view: UserView)
    suspend fun save(passwordOld: String, passwordNew: String)

}