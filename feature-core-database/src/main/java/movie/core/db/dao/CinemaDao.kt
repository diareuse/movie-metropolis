package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.CinemaStored

@Dao
interface CinemaDao : DaoBase<CinemaStored> {

    @Query("select * from cinemas order by city asc")
    suspend fun selectAll(): List<CinemaStored>

    @Query("select * from cinemas where id=:id")
    suspend fun select(id: String): CinemaStored

}