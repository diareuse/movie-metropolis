package movie.image.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ImageStored::class,
        ColorStored::class
    ],
    version = 1
)
internal abstract class ImageDatabase : RoomDatabase() {

    abstract fun analysis(): AnalysisDao
    abstract fun image(): ImageDao
    abstract fun color(): ColorDao

}