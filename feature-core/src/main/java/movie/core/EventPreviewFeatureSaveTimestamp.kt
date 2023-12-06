package movie.core

import movie.core.nwk.model.ShowingType
import movie.core.preference.SyncPreference
import java.util.Date

class EventPreviewFeatureSaveTimestamp(
    private val origin: EventPreviewFeature,
    private val preference: SyncPreference,
    private val type: ShowingType
) : EventPreviewFeature {

    override suspend fun get() = origin.get().also {
        @Suppress("UNUSED_VARIABLE")
        val ignore = when (type) {
            ShowingType.Current -> preference.previewCurrent = Date()
            ShowingType.Upcoming -> preference.previewUpcoming = Date()
        }
    }

}