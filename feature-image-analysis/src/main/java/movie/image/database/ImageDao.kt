package movie.image.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
internal interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: ImageStored)

}