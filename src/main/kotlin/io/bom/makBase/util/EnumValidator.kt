package io.bom.makBase.util

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator: ConstraintValidator<EnumValid, CharSequence> {
    private val acceptedValues: MutableList<String> = mutableListOf()

    override fun initialize(constraintAnnotation: EnumValid) {
        super.initialize(constraintAnnotation)

        acceptedValues.addAll(constraintAnnotation.enumClass.java
            .enumConstants
            .map {it.name}
        )
    }

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext): Boolean {
        return if (value == null) {
            true
        } else acceptedValues.contains(value.toString())

    }
}
