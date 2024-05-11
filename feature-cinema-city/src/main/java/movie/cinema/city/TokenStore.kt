package movie.cinema.city

interface TokenStore {
    var token: String
    var refreshToken: String
}