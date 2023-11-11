package movie.rating.database

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface ActorReferenceDao : BaseDao<ActorReferenceStored> {
    @Query("select * from actor_references as ar join actor_reference_connections as arc on arc.movie=ar.id where arc.actor=:actor")
    suspend fun selectRelated(actor: Long): List<ActorReferenceStored>
}