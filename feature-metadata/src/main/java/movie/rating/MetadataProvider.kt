package movie.rating

interface MetadataProvider {

    suspend fun get(descriptor: MovieDescriptor): MovieMetadata?

}