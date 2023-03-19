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

    override suspend fun get(): Result<User> {
        var user: Result<User> = Result.failure(IllegalStateException("Chain is empty"))
        for (link in links) {
            val next = link.get()
            if (next.isSuccess)
                user = link.get()
        }
        return user
    }

}