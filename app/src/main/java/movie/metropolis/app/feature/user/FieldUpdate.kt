package movie.metropolis.app.feature.user

sealed class FieldUpdate {

    sealed class Consent : FieldUpdate() {

        data class Marketing(val isEnabled: Boolean) : Consent()
        data class Premium(val isEnabled: Boolean) : Consent()

    }

    data class Email(val value: String) : FieldUpdate()
    data class Cinema(val id: String) : FieldUpdate()

    sealed class Name : FieldUpdate() {

        data class First(val value: String) : Name()
        data class Last(val value: String) : Name()

    }

    data class Phone(val value: String) : FieldUpdate()
    data class Password(
        val old: String,
        val new: String
    ) : FieldUpdate() {
        val isValid get() = old.isNotBlank() && new.isNotBlank() && new != old
    }

}