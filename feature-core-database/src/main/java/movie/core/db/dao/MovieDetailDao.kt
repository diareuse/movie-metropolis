package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MovieDetailStored
import movie.core.db.model.MovieDetailView

@Dao
interface MovieDetailDao : DaoBase<MovieDetailStored> {

    @Query("select * from movie_detail_views where id=:id")
    suspend fun select(id: String): MovieDetailView

}