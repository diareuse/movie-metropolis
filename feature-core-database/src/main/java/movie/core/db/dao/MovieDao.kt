package movie.core.db.dao

import androidx.room.Dao
import movie.core.db.model.MovieStored

@Dao
interface MovieDao : DaoBase<MovieStored>