package com.typewritermc.processors.entry.modifiers

import com.google.devtools.ksp.processing.Resolver
import com.typewritermc.core.extension.annotations.Negative
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataModifier
import com.typewritermc.processors.entry.DataModifierComputer
import com.typewritermc.processors.entry.PrimitiveType
import kotlin.reflect.KClass

object NegativeModifierComputer : DataModifierComputer<Negative> {
    override val annotationClass: KClass<Negative> = Negative::class

    context(Resolver)
    override fun compute(blueprint: DataBlueprint, annotation: Negative): Result<DataModifier> {
        // If the field is wrapped in a list or other container, we try if the inner type can be modified
        innerCompute(blueprint, annotation)?.let { return ok(it) }

        if (blueprint !is DataBlueprint.PrimitiveBlueprint) {
            return failure("Negative annotation can only be used on numbers (including in lists or maps)!")
        }

        if (blueprint.type != PrimitiveType.INTEGER && blueprint.type != PrimitiveType.DOUBLE) {
            return failure("Negative annotation can only be used on numbers (including in lists or maps)!")
        }

        return ok(DataModifier.Modifier("negative"))
    }
}