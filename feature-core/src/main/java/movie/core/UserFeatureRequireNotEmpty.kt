package movie.core

class UserFeatureRequireNotEmpty(
    private val origin: UserFeature
) : UserFeature by origin {

    @Suppress("ReplaceSizeCheckWithIsNotEmpty")
    override suspend fun getBookings() = origin.getBookings()
        .mapCatching { require(it.count() > 0); it }

}