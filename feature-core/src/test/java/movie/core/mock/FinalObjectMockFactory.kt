package movie.core.mock

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

class FinalObjectMockFactory<T : Any>(
    private val type: KClass<T>
) {

    private val constructor get() = requireNotNull(type.primaryConstructor)
    private val overrides = mutableMapOf<KParameter, Any?>()
    private val defaults = mutableMapOf<KType, Any?>()

    private val KParameter.defaultParam: Any?
        get() = when {
            type.isMarkedNullable -> null
            type.isOrIsSubtypeOf<Byte>() -> 0.toByte()
            type.isOrIsSubtypeOf<Double>() -> 0.0
            type.isOrIsSubtypeOf<Float>() -> 0f
            type.isOrIsSubtypeOf<Int>() -> 0
            type.isOrIsSubtypeOf<Long>() -> 0L
            type.isOrIsSubtypeOf<Short>() -> 0.toShort()
            type.isOrIsSubtypeOf<String>() -> ""
            type.isOrIsSubtypeOf<Array<*>>() -> emptyArray<Any>()
            type.isOrIsSubtypeOf<Iterable<*>>() -> emptyList<Any>()
            type.isOrIsSubtypeOf<Sequence<*>>() -> emptySequence<Any>()
            else -> defaults.getValue(type)
        }

    private inline fun <reified T : Any> KType.isOrIsSubtypeOf(): Boolean {
        return this == T::class || this.isSubtypeOf(T::class.starProjectedType)
    }

    fun override(type: KType, value: Any?) {
        defaults[type] = value
    }

    operator fun set(name: String, value: Any?) {
        val parameter = checkNotNull(constructor.findParameterByName(name))
        overrides[parameter] = value
    }

    operator fun set(property: KProperty<*>, value: Any?) = set(property.name, value)

    fun create(): T {
        val constructorParameters = constructor.parameters.associateWith {
            overrides.getOrElse(it) { it.defaultParam }
        }
        return constructor.callBy(constructorParameters)
    }

}

inline fun <reified T> FinalObjectMockFactory<*>.override(value: () -> T) {
    override(T::class.starProjectedType, value())
}

inline fun <reified T : Any> mockFinal(
    body: FinalObjectMockFactory<T>.() -> Unit
) = FinalObjectMockFactory(T::class)
    .apply(body)
    .create()
