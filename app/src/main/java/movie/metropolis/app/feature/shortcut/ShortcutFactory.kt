package movie.metropolis.app.feature.shortcut

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import movie.metropolis.app.MainActivity

interface ShortcutFactory {

    fun setId(id: String): ShortcutFactory
    fun setLabel(label: String): ShortcutFactory
    fun setIcon(icon: Int): ShortcutFactory
    fun setRoute(deepLink: Uri): ShortcutFactory
    fun create()

    companion object {

        fun default(context: Context): ShortcutFactory {
            return ShortcutFactoryDefault(context)
        }

        fun unique(context: Context): ShortcutFactory {
            return ShortcutFactoryUnique(ShortcutFactoryDefault(context))
        }

    }

}

private class ShortcutFactoryUnique(
    private val origin: ShortcutFactoryDefault
) : ShortcutFactory {

    override fun setId(id: String) = apply {
        origin.setId(id)
    }

    override fun setLabel(label: String) = apply {
        origin.setLabel(label)
    }

    override fun setIcon(icon: Int) = apply {
        origin.setIcon(icon)
    }

    override fun setRoute(deepLink: Uri) = apply {
        origin.setRoute(deepLink)
    }

    override fun create() {
        val shortcuts = ShortcutManagerCompat.getDynamicShortcuts(origin.context)
        val removable = shortcuts.asSequence()
            .filter { it.id == origin.id }
            .map { it.id }
            .toList()
        ShortcutManagerCompat.removeDynamicShortcuts(origin.context, removable)
        origin.create()
    }

}

private class ShortcutFactoryDefault(
    val context: Context
) : ShortcutFactory {

    private lateinit var intent: Intent
    private lateinit var icon: IconCompat
    private lateinit var label: String
    lateinit var id: String

    override fun setId(id: String) = apply {
        this.id = id
    }

    override fun setLabel(label: String) = apply {
        this.label = label
    }

    override fun setIcon(icon: Int) = apply {
        this.icon = IconCompat.createWithResource(context, icon)
    }

    override fun setRoute(deepLink: Uri) = apply {
        this.intent = Intent(context, MainActivity::class.java)
            .setAction(Intent.ACTION_VIEW)
            .setData(deepLink)
            .setPackage(context.packageName)
    }

    override fun create() {
        val shortcut = ShortcutInfoCompat.Builder(context, id)
            .setShortLabel(label)
            .setLongLabel(label)
            .setIcon(icon)
            .setIntent(intent)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

}