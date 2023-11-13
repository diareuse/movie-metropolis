package movie.metropolis.app.presentation.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import movie.core.ResultCallback
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.asLoadable
import movie.metropolis.app.util.throttleWithTimeout
import kotlin.time.Duration.Companion.seconds

interface ProfileFacade {

    suspend fun getCinemas(callback: ResultCallback<List<CinemaSimpleView>>)
    suspend fun getMembership(callback: ResultCallback<MembershipView?>)
    suspend fun getUser(callback: ResultCallback<UserView>)
    suspend fun isLoggedIn(): Boolean

    suspend fun save(view: UserView)
    suspend fun save(passwordOld: String, passwordNew: String)

    companion object {

        suspend fun ProfileFacade.getMembership(): Result<MembershipView?> {
            var out: Result<MembershipView?> = Result.failure(IllegalStateException())
            getMembership { out = it }
            return out
        }

        suspend fun ProfileFacade.getUser(): Result<UserView> {
            var out: Result<UserView> = Result.failure(IllegalStateException())
            getUser { out = it }
            return out
        }

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
            }.throttleWithTimeout(1.seconds)

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
        }.throttleWithTimeout(1.seconds)

    }

}