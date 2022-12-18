package movie.core.notification

import android.app.PendingIntent
import movie.core.model.Movie

interface NotificationInfoProvider {

    fun getChannel(): String
    fun getDeepLink(movie: Movie): PendingIntent

}