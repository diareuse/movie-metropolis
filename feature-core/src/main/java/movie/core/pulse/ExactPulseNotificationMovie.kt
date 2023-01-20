package movie.core.pulse

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import movie.core.EventDetailFeature
import movie.core.EventDetailFeature.Companion.get
import movie.core.FavoriteFeature
import movie.core.R
import movie.core.adapter.MovieFromId
import movie.core.adapter.MoviePreviewFromDetail
import movie.core.model.Media
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.notification.NotificationInfoProvider
import movie.pulse.ExactPulseCoroutine
import java.net.URL

class ExactPulseNotificationMovie(
    private val detail: EventDetailFeature,
    private val info: NotificationInfoProvider,
    private val favorite: FavoriteFeature
) : ExactPulseCoroutine() {

    override suspend fun executeAsync(context: Context, data: Data) {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return
        val id = data.getString(ExtraId).let(::checkNotNull)
        val movie = detail.get(MovieFromId(id)).getOrNull() ?: return
        val notification = getNotification(context, movie)
        manager.notify(movie.id.hashCode(), notification)
        favorite.toggle(MoviePreviewFromDetail(movie))
    }

    private fun getNotification(context: Context, movie: MovieDetail): Notification {
        val style = NotificationCompat.BigPictureStyle()
            .setBigContentTitle("Now available!")
            .setSummaryText("${movie.name} is now available at the cinemas.")
            .bigPicture(getPicture(movie))
        return NotificationCompat.Builder(context, info.getChannel())
            .setContentTitle("Now available!")
            .setContentText("${movie.name} is now available at the cinemas.")
            .setSmallIcon(R.drawable.ic_stat_movie)
            .setContentIntent(info.getDeepLink(movie))
            .setStyle(style)
            .setColorized(true)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .build()
    }

    private fun getPicture(movie: MovieDetail): Bitmap? {
        val image = movie.media
            .asSequence()
            .filterIsInstance<Media.Image>()
            .maxByOrNull { it.width * it.height }
            ?: return null
        return URL(image.url).openStream().use {
            BitmapFactory.decodeStream(it)
        }
    }

    companion object {

        private const val ExtraId = "movie.core.EXTRA_ID"

        fun getData(movie: Movie) = Data.Builder()
            .putString(ExtraId, movie.id)
            .build()

    }

}