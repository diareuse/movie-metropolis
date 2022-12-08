package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MoviePreviewStored
import movie.core.db.model.MoviePreviewView

@Dao
interface MoviePreviewDao : DaoBase<MoviePreviewStored> {

    @Query("select * from movie_preview_views where (select movie_previews.upcoming from movie_previews where movie_previews.movie=movie_preview_views.id)")
    suspend fun selectUpcoming(): List<MoviePreviewView>

    @Query("select * from movie_preview_views where not (select movie_previews.upcoming from movie_previews where movie_previews.movie=movie_preview_views.id)")
    suspend fun selectCurrent(): List<MoviePreviewView>

}