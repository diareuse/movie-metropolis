package movie.metropolis.app.screen.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import kotlin.time.Duration.Companion.seconds

interface ProfileFacade {

    suspend fun getCinemas(): Result<List<CinemaSimpleView>>
    suspend fun getMembership(): Result<MembershipView?>
    suspend fun getUser(): Result<UserView>
    suspend fun isLoggedIn(): Boolean

    suspend fun save(view: UserView): Result<UserView>
    suspend fun save(passwordOld: String, passwordNew: String): Result<Unit>

    companion object {

        val ProfileFacade.cinemasFlow
            get() = channelFlow {
                send(getCinemas().asLoadable())
            }

        val ProfileFacade.membershipFlow
            get() = channelFlow {
                send(getMembership().asLoadable())
            }

        fun ProfileFacade.userFlow(jobEmitter: Flow<suspend () -> Unit>) = channelFlow {
            send(getUser().asLoadable())
            jobEmitter.collect {
                send(Loadable.loading())
                it()
                send(getUser().asLoadable())
            }
        }.debounce(1.seconds)

    }

}