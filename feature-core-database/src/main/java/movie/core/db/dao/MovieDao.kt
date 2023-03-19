package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MovieStored

@Dao
interface MovieDao : DaoBase<MovieStored> {

    @Query("select duration from movies where id=:id")
    suspend fun getDuration(id: String): Long

}