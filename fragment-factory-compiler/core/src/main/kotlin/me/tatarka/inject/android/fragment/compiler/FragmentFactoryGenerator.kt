package me.tatarka.inject.android.fragment.compiler

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import me.tatarka.inject.compiler.AstClass
import me.tatarka.inject.compiler.INTO_MAP
import me.tatarka.inject.compiler.OutputProvider
import me.tatarka.inject.compiler.PROVIDES
import java.util.Locale

private const val ANDROID_ANNOTATION_PACKAGE_NAME = "me.tatarka.inject.android.annotations"
val GENERATE_FRAGMENT_FACTORY =
    ClassName(ANDROID_ANNOTATION_PACKAGE_NAME, "GenerateFragmentFactory")
val FRAGMENT = ClassName("androidx.fragment.app", "Fragment")

private val FRAGMENT_ENTRY = Pair::class.asClassName().parameterizedBy(
    STRING, LambdaTypeName.get(returnType = FRAGMENT)
)

class FragmentFactoryGenerator(private val provider: OutputProvider) {

    fun generate(astClass: AstClass, injectFragments: List<AstClass>): FileSpec {
        val generatedClassName = "FragmentFactory${astClass.name}"
        return with(provider) {
            FileSpec.builder(astClass.packageName, generatedClassName)
                .addType(
                    TypeSpec.interfaceBuilder(generatedClassName).apply {
                        if (injectFragments.isNotEmpty()) {
                            for (fragment in injectFragments) {
                                val name = fragment.name.decapitalize(Locale.US)
                                val type = fragment.asClassName()
                                addProperty(
                                    PropertySpec.builder(name, FRAGMENT_ENTRY)
                                        .receiver(LambdaTypeName.get(returnType = type))
                                        .getter(
                                            FunSpec.getterBuilder().addAnnotation(PROVIDES)
                                                .addAnnotation(INTO_MAP)
                                                .addStatement(
                                                    "return %T::class.java.name to this",
                                                    type
                                                ).build()
                                        ).build()
                                )
                            }
                        } else {
                            addProperty(
                                PropertySpec.builder(
                                    "noFragments",
                                    ClassName("kotlin.collections", "Map").parameterizedBy(
                                        ClassName("kotlin", "String"),
                                        LambdaTypeName.get(returnType = FRAGMENT)
                                    )
                                ).getter(
                                    FunSpec.getterBuilder().addAnnotation(PROVIDES)
                                        .addStatement("return emptyMap()").build()
                                ).build()
                            )
                        }
                    }.build(listOf(astClass) + injectFragments)
                ).build()
        }
    }
}