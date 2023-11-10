package movie.metropolis.app.feature.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationChannelGroupCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import movie.core.model.Movie
import movie.core.notification.NotificationInfoProvider
import movie.metropolis.app.MainActivity2
import movie.metropolis.app.R
import movie.metropolis.app.screen.Route

class NotificationInfoProviderCompound(
    private val context: Context
) : NotificationInfoProvider {

    private val manager = NotificationManagerCompat.from(context)

    override fun getChannel(): String {
        return getOrCreateChannel(manager)
    }

    override fun getDeepLink(movie: Movie): PendingIntent {
        val intent = Intent(context, MainActivity2::class.java)
            .setAction(Intent.ACTION_VIEW)
            .setData(Route.Movie.deepLink(movie.id))
        return TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(intent)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
            .let(::checkNotNull)
    }

    // ---

    private fun getOrCreateChannel(manager: NotificationManagerCompat): String {
        val id = "movie.metropolis.NOW_AVAILABLE"
        val hasChannel = manager.notificationChannelsCompat.any { it.id == id }
        if (hasChannel) return id
        val channel = NotificationChannelCompat
            .Builder(id, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setGroup(getOrCreateGroup(manager))
            .setShowBadge(true)
            .setName(context.getString(R.string.stat_channel_now_available_title))
            .setDescription(context.getString(R.string.stat_channel_now_available_desc))
            .build()
        manager.createNotificationChannel(channel)
        return channel.id
    }

    private fun getOrCreateGroup(manager: NotificationManagerCompat): String {
        val id = "movie.metropolis.GROUP_MOVIE"
        val hasGroup = manager.notificationChannelGroupsCompat.any { it.id == id }
        if (hasGroup) return id
        val group = NotificationChannelGroupCompat.Builder(id)
            .setName(context.getString(R.string.stat_group_movie_title))
            .setDescription(context.getString(R.string.stat_group_movie_desc))
            .build()
        manager.createNotificationChannelGroup(group)
        return group.id
    }

}