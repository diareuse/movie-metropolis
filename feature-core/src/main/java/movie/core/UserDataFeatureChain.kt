package movie.core

import movie.core.model.FieldUpdate
import movie.core.model.User

class UserDataFeatureChain(
    private vararg val links: UserDataFeature
) : UserDataFeature {

    override suspend fun update(data: Iterable<FieldUpdate>) {
        val chainError = Throwable()
        for (link in links) try {
            link.update(data)
        } catch (e: Throwable) {
            chainError.addSuppressed(e)
            continue
        }
        if (chainError.suppressed.isNotEmpty())
            throw chainError
    }

    override suspend fun get(): User {
        var user: User? = null
        for (link in links) {
            user = link.get()
        }
        return user!!
    }

}