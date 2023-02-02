package movie.core

import kotlinx.coroutines.coroutineScope
import movie.core.model.FieldUpdate
import movie.core.model.User
import movie.core.preference.UserPreference

class UserDataFeatureStoring(
    private val origin: UserDataFeature,
    private val preference: UserPreference,
) : UserDataFeature by origin {

    override suspend fun update(data: Iterable<FieldUpdate>) {
        origin.update(data)
        for (item in data) when (item) {
            is FieldUpdate.Cinema -> preference.favorite = item.id
            is FieldUpdate.Consent.Marketing -> preference.consentMarketing = item.isEnabled
            is FieldUpdate.Consent.Premium -> preference.consentPremium = item.isEnabled
            is FieldUpdate.Email -> preference.email = item.value
            is FieldUpdate.Name.First -> preference.firstName = item.value
            is FieldUpdate.Name.Last -> preference.lastName = item.value
            is FieldUpdate.Phone -> preference.phone = item.value
            else -> Unit
        }
    }

    override suspend fun get(callback: ResultCallback<User>) = coroutineScope {
        origin.get(callback.then(this) {
            preference.set(it)
        })
    }

}