package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PosterDao {

    @Query("select mm.url from movie_media as mm order by mm.width*mm.height desc")
    suspend fun selectAll(): List<String>

}