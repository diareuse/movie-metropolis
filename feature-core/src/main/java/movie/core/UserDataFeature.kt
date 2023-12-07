package movie.core

import movie.core.model.FieldUpdate
import movie.core.model.User

interface UserDataFeature {
    suspend fun update(data: Iterable<FieldUpdate>)
    suspend fun get(): User
}