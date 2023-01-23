package movie.core

import movie.core.model.FieldUpdate
import movie.core.model.User

interface UserDataFeature {
    suspend fun update(data: Iterable<FieldUpdate>): Result<User>
    suspend fun get(callback: ResultCallback<User>)
}