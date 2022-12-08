package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MovieReferenceStored
import movie.core.db.model.MovieReferenceView

@Dao
interface MovieReferenceDao : DaoBase<MovieReferenceStored> {

    @Query("select * from movie_reference_views where id=:id")
    suspend fun select(id: String): MovieReferenceView

}

