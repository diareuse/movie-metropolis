package movie.rating.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(DateTypeConverter::class)
@Database(
    entities = [
        RatingStored::class,
        ActorStored::class,
        ActorReferenceConnection::class,
        ActorReferenceStored::class
    ],
    version = 3
)
internal abstract class MetadataDatabase : RoomDatabase() {

    abstract fun rating(): RatingDao
    abstract fun actor(): ActorDao
    abstract fun actorReferenceConnection(): ActorReferenceConnectionDao
    abstract fun actorReference(): ActorReferenceDao

}