package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MovieRatingStored

@Dao
interface MovieRatingDao : DaoBase<MovieRatingStored> {

    @Query("select rating from movie_ratings where movie=:id")
    suspend fun select(id: String): Byte?

}