package movie.cinema.city

interface CredentialsProvider {
    suspend fun get(): Credentials
}