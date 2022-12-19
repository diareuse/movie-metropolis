package movie.core.db.dao

import androidx.room.Dao
import movie.core.db.model.MovieRatingStored

@Dao
interface MovieRatingDao : DaoBase<MovieRatingStored>