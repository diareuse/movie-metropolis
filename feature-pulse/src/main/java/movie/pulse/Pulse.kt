package movie.pulse

interface Pulse {

    suspend fun execute(): Result<Any>

}