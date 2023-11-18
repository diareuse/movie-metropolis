package movie.core.pulse

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import movie.core.EventDetailFeature
import movie.core.FavoriteFeature
import movie.core.R
import movie.core.adapter.MovieFromId
import movie.core.model.Media
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.notification.NotificationInfoProvider
import movie.image.ImageAnalyzer
import movie.pulse.ExactPulseCoroutine
import java.net.URL

class ExactPulseNotificationMovie(
    private val detail: EventDetailFeature,
    private val info: NotificationInfoProvider,
    private val favorite: FavoriteFeature,
    private val image: ImageAnalyzer
) : ExactPulseCoroutine() {

    @SuppressLint("MissingPermission")
    override suspend fun executeAsync(context: Context, data: Data) {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return
        val id = data.getString(ExtraId).let(::checkNotNull)
        val movie = detail.get(MovieFromId(id)).getOrNull() ?: return
        val notification = getNotification(context, movie)
        manager.notify(movie.id.hashCode(), notification)
        favorite.setNotified(movie)
    }

    private suspend fun getNotification(context: Context, movie: MovieDetail): Notification {
        val imageUrl = getPictureUrl(movie)
        val color = if (imageUrl != null) image.getColors(imageUrl).vibrant.rgb else Color.BLACK
        val image = getPicture(imageUrl)
        var style = NotificationCompat.BigPictureStyle()
            .setBigContentTitle(context.getString(R.string.now_available))
            .setSummaryText(context.getString(R.string.now_available_description, movie.name))
            .bigPicture(image)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            style = style.showBigPictureWhenCollapsed(true)
        }
        return NotificationCompat.Builder(context, info.getChannel())
            .setContentTitle(context.getString(R.string.now_available))
            .setContentText(context.getString(R.string.now_available_description, movie.name))
            .setSmallIcon(R.drawable.ic_stat_movie)
            .setContentIntent(info.getDeepLink(movie))
            .setColor(color)
            .setStyle(style)
            .setColorized(true)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .build()
    }

    private fun getPictureUrl(movie: MovieDetail) = movie.media
        .asSequence()
        .filterIsInstance<Media.Image>()
        .maxByOrNull { it.width * it.height }
        ?.url

    private fun getPicture(url: String?): Bitmap? {
        return URL(url ?: return null).openStream().use {
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