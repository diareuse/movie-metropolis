package movie.pulse

interface ExactPulseScheduler {
    fun schedule(request: ExactPulseRequest)
    fun cancel(request: ExactPulseRequest)
}