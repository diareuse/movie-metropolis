package movie.metropolis.app.screen.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import movie.core.ResultCallback
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import kotlin.time.Duration.Companion.seconds

interface ProfileFacade {

    suspend fun getCinemas(callback: ResultCallback<List<CinemaSimpleView>>)
    suspend fun getMembership(callback: ResultCallback<MembershipView?>)
    suspend fun getUser(callback: ResultCallback<UserView>)
    suspend fun isLoggedIn(): Boolean

    suspend fun save(view: UserView)
    suspend fun save(passwordOld: String, passwordNew: String)

    companion object {

        val ProfileFacade.cinemasFlow
            get() = channelFlow {
                getCinemas {
                    send(it.asLoadable())
                }
            }

        val ProfileFacade.membershipFlow
            get() = channelFlow {
                getMembership {
                    send(it.asLoadable())
                }
            }

        fun ProfileFacade.userFlow(jobEmitter: Flow<suspend () -> Unit>) = channelFlow {
            getUser {
                send(it.asLoadable())
            }
            jobEmitter.collect {
                send(Loadable.loading())
                it()
                getUser {
                    send(it.asLoadable())
                }
            }
        }.debounce(1.seconds)

    }

}