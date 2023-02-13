package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MovieRatingStored

@Dao
interface MovieRatingDao : DaoBase<MovieRatingStored> {

    @Query("select rating from movie_ratings where movie=:id")
    suspend fun select(id: String): Byte?

    @Query("select exists(select 1 from movie_ratings where movie=:id and created_at > (STRFTIME('%s', 'now') * 1000))")
    suspend fun isRecent(id: String): Boolean

}