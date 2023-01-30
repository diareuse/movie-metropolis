package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MoviePromoStored

@Dao
interface MoviePromoDao : DaoBase<MoviePromoStored> {

    @Query("select url from movie_promos where movie=:id")
    suspend fun select(id: String): String?

}