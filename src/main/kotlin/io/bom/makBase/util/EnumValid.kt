package io.bom.makBase.util

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD,)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EnumValidator::class])
annotation class EnumValid(
    val enumClass: KClass<out Enum<*>>,
    val message: String ="INVALID ENUM VALUE",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
