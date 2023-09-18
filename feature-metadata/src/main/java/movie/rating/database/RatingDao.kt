package movie.rating.database

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface RatingDao : BaseDao<RatingStored> {

    @Query("select * from ratings where name=:name and year=:year order by rating desc limit 1")
    suspend fun select(name: String, year: Int): RatingStored?

}