package movie.rating.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

internal interface BaseDao<T> {

    @Insert
    suspend fun insert(model: T)

    @Update
    suspend fun update(model: T)

    @Delete
    suspend fun delete(model: T)

    suspend fun insertOrUpdate(model: T) = try {
        insert(model)
    } catch (e: Throwable) {
        update(model)
    }

}