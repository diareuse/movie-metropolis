package movie.core.preference

import movie.core.model.User
import java.util.Date

interface UserPreference {

    var firstName: String
    var lastName: String
    var email: String
    var phone: String
    var favorite: String?
    var points: Double
    var consentMarketing: Boolean
    var consentPremium: Boolean
    var cardNumber: String?
    var memberFrom: Date?
    var memberUntil: Date?

    fun set(user: User)

}

