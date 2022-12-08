package movie.metropolis.app.screen.profile

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
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
            get() = flow {
                emit(getCinemas().asLoadable())
            }

        val ProfileFacade.membershipFlow
            get() = flow {
                emit(getMembership().asLoadable())
            }

        fun ProfileFacade.userFlow(jobEmitter: Flow<suspend () -> Unit>) = flow {
            emit(getUser().asLoadable())
            jobEmitter.collect {
                emit(Loadable.loading())
                it()
                emit(getUser().asLoadable())
            }
        }

        val ProfileFacade.isLoggedInFlow
            get() = flow {
                emit(Loadable.success(isLoggedIn()))
                while (currentCoroutineContext().isActive) {
                    delay(1.seconds)
                    emit(Loadable.success(isLoggedIn()))
                }
            }.distinctUntilChanged()

    }

}