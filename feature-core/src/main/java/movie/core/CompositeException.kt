package movie.core

import java.io.PrintStream
import java.io.PrintWriter

class CompositeException(
    private vararg val causes: Throwable
) : RuntimeException() {

    override fun printStackTrace(s: PrintStream) {
        for (cause in causes)
            cause.printStackTrace(s)
    }

    override fun printStackTrace(s: PrintWriter) {
        for (cause in causes)
            cause.printStackTrace(s)
    }

}