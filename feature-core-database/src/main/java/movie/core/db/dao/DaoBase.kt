package movie.core.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface DaoBase<T> {

    @Insert
    suspend fun insert(model: T)

    @Delete
    suspend fun delete(model: T)

    @Update
    suspend fun update(model: T)

    suspend fun insertOrUpdate(model: T) = insertOrElse(model) {
        update(model)
    }

    companion object {
        suspend inline fun <T> DaoBase<T>.insertOrElse(model: T, otherwise: (T) -> Unit) {
            try {
                insert(model)
            } catch (e: Throwable) {
                otherwise(model)
            }
        }
    }

}