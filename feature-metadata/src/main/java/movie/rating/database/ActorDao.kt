package movie.rating.database

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface ActorDao : BaseDao<ActorStored> {
    @Query("select * from actors as a where a.`query`=:query limit 1")
    suspend fun selectBy(query: String): ActorStored?
}