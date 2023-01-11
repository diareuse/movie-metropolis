package movie.log

interface Logger {

    fun verbose(tag: String, message: String, throwable: Throwable? = null)
    fun debug(tag: String, message: String, throwable: Throwable? = null)
    fun info(tag: String, message: String, throwable: Throwable? = null)
    fun warn(tag: String, message: String, throwable: Throwable? = null)
    fun error(tag: String, message: String, throwable: Throwable? = null)

    companion object : Logger {

        private var logger: Logger = DefaultLogger()

        fun setLogger(logger: Logger) {
            this.logger = logger
        }

        override fun verbose(tag: String, message: String, throwable: Throwable?) {
            logger.verbose(tag, message, throwable)
        }

        override fun debug(tag: String, message: String, throwable: Throwable?) {
            logger.debug(tag, message, throwable)
        }

        override fun info(tag: String, message: String, throwable: Throwable?) {
            logger.info(tag, message, throwable)
        }

        override fun warn(tag: String, message: String, throwable: Throwable?) {
            logger.warn(tag, message, throwable)
        }

        override fun error(tag: String, message: String, throwable: Throwable?) {
            logger.error(tag, message, throwable)
        }

    }

}