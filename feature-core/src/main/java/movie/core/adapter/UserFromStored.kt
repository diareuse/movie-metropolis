package movie.core.adapter

import movie.core.model.Cinema
import movie.core.model.User
import movie.core.preference.UserPreference
import java.util.Date

data class UserFromStored(
    override val firstName: String,
    override val lastName: String,
    override val email: String,
    override val phone: String,
    override val favorite: Cinema?,
    override val consent: User.Consent,
    override val membership: User.Membership?,
    override val points: Double
) : User {

    constructor(
        preference: UserPreference,
        cinemas: Iterable<Cinema>
    ) : this(
        firstName = preference.firstName,
        lastName = preference.lastName,
        email = preference.email,
        phone = preference.phone,
        favorite = cinemas.firstOrNull { it.id == preference.favorite },
        consent = Consent(preference),
        membership = Membership(preference),
        points = preference.points,
    )

    data class Consent(
        override val marketing: Boolean,
        override val premium: Boolean
    ) : User.Consent {

        constructor(
            preference: UserPreference
        ) : this(
            preference.consentMarketing,
            preference.consentPremium
        )

    }

    data class Membership(
        override val cardNumber: String,
        override val memberFrom: Date,
        override val memberUntil: Date
    ) : User.Membership {

        companion object {

            operator fun invoke(preference: UserPreference): Membership? {
                val cardNumber = preference.cardNumber ?: return null
                val memberFrom = preference.memberFrom ?: return null
                val memberUntil = preference.memberUntil ?: return null
                return Membership(cardNumber, memberFrom, memberUntil)
            }

        }

    }

}