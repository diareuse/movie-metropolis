package movie.core.preference

import movie.core.model.User
import movie.settings.PreferenceStore
import java.util.Date

class UserPreferenceStored(
    private val store: PreferenceStore
) : UserPreference {

    override var firstName: String
        get() = store["name-first"].orEmpty()
        set(value) {
            store["name-first"] = value.takeUnless { it.isBlank() }
        }
    override var lastName: String
        get() = store["name-last"].orEmpty()
        set(value) {
            store["name-last"] = value.takeUnless { it.isBlank() }
        }
    override var email: String
        get() = store["email"].orEmpty()
        set(value) {
            store["email"] = value.takeUnless { it.isBlank() }
        }
    override var phone: String
        get() = store["phone"].orEmpty()
        set(value) {
            store["phone"] = value.takeUnless { it.isBlank() }
        }
    override var favorite: String?
        get() = store["cinema"]
        set(value) {
            store["cinema"] = value
        }
    override var points: Double
        get() = store["points"]?.toDoubleOrNull() ?: 0.0
        set(value) {
            store["points"] = value.toString()
        }
    override var consentMarketing: Boolean
        get() = store["consent-marketing"].toBoolean()
        set(value) {
            store["consent-marketing"] = value.toString()
        }
    override var consentPremium: Boolean
        get() = store["consent-premium"].toBoolean()
        set(value) {
            store["consent-premium"] = value.toString()
        }
    override var cardNumber: String?
        get() = store["card-number"]
        set(value) {
            store["card-number"] = value
        }
    override var memberFrom: Date?
        get() = store["member-from"]?.toLongOrNull()?.let(::Date)
        set(value) {
            store["member-from"] = value?.time?.toString()
        }
    override var memberUntil: Date?
        get() = store["member-until"]?.toLongOrNull()?.let(::Date)
        set(value) {
            store["member-until"] = value?.time?.toString()
        }

    override fun set(user: User) {
        firstName = user.firstName
        lastName = user.lastName
        email = user.email
        phone = user.phone
        favorite = user.favorite?.id
        points = user.points
        consentMarketing = user.consent.marketing
        consentPremium = user.consent.premium
        cardNumber = user.membership?.cardNumber
        memberFrom = user.membership?.memberFrom
        memberUntil = user.membership?.memberUntil
    }

}