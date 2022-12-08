package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MovieMediaStored
import movie.core.db.model.MovieMediaView

@Dao
interface MovieMediaDao : DaoBase<MovieMediaStored> {

    @Query("select width,height,url,type from movie_media where movie=:id")
    suspend fun select(id: String): List<MovieMediaView>

}