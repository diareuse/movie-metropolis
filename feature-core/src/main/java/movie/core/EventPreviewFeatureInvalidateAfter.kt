package movie.core

import movie.core.model.MoviePreview
import movie.core.nwk.model.ShowingType
import movie.core.preference.SyncPreference
import movie.core.preference.SyncPreference.Companion.isInThreshold
import kotlin.time.Duration

class EventPreviewFeatureInvalidateAfter(
    private val origin: EventPreviewFeature,
    private val preference: SyncPreference,
    private val type: ShowingType,
    private val duration: Duration
) : EventPreviewFeature {

    override suspend fun get(): Sequence<MoviePreview> {
        val lastRefresh = when (type) {
            ShowingType.Current -> preference.previewCurrent
            ShowingType.Upcoming -> preference.previewUpcoming
        }
        if (!lastRefresh.isInThreshold(duration)) {
            throw ExpiredException(lastRefresh, duration)
        }
        return origin.get()
    }

}

