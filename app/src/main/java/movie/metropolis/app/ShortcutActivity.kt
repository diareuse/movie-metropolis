package movie.metropolis.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri

class ShortcutActivity : Activity() {

    private val route
        get() = when (intent.action) {
            "movie.metropolis.app.ACTION_TICKETS" -> "home?screen=tickets"
            else -> "home"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
            .setAction(Intent.ACTION_VIEW)
            .setData("app://movie.metropolis/$route".toUri())
            .setPackage(packageName)
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

}