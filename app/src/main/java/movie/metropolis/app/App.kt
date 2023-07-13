package movie.metropolis.app

import android.app.Application
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val threadPolicy = StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build()
        StrictMode.setThreadPolicy(threadPolicy)
    }

}
