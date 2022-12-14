package movie.log

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class ExceptionLogger(
    crashlytics: () -> FirebaseCrashlytics = { Firebase.crashlytics }
) : Logger {

    private val crashlytics by lazy(crashlytics)

    override fun verbose(tag: String, message: String, throwable: Throwable?) {
        crashlytics.log(message)
    }

    override fun debug(tag: String, message: String, throwable: Throwable?) {
        crashlytics.log(message)
    }

    override fun info(tag: String, message: String, throwable: Throwable?) {
        crashlytics.log(message)
    }

    override fun warn(tag: String, message: String, throwable: Throwable?) {
        crashlytics.log(message)
        crashlytics.recordException(throwable ?: return)
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        crashlytics.log(message)
        crashlytics.recordException(throwable ?: return)
    }

}