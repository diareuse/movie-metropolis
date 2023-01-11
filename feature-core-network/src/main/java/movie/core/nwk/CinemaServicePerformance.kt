package movie.core.nwk

class CinemaServicePerformance(
    private val origin: CinemaService,
    private val tracer: PerformanceTracer
) : CinemaService {

    override suspend fun getCinemas() = tracer.trace("api.cinema") {
        origin.getCinemas().also { result ->
            it.setState(result.isSuccess)
        }
    }

}