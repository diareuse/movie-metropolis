package movie.log

class PlatformLogger : Logger {

    override fun verbose(tag: String, message: String, throwable: Throwable?) {
        println("V/$tag: $message")
        throwable?.printStackTrace()
    }

    override fun debug(tag: String, message: String, throwable: Throwable?) {
        println("D/$tag: $message")
        throwable?.printStackTrace()
    }

    override fun info(tag: String, message: String, throwable: Throwable?) {
        println("I/$tag: $message")
        throwable?.printStackTrace()
    }

    override fun warn(tag: String, message: String, throwable: Throwable?) {
        println("W/$tag: $message")
        throwable?.printStackTrace()
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        println("E/$tag: $message")
        throwable?.printStackTrace()
    }

}