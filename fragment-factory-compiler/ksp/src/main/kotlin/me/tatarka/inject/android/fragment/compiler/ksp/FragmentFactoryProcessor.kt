package me.tatarka.inject.android.fragment.compiler.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.writeTo
import me.tatarka.inject.android.fragment.compiler.FRAGMENT
import me.tatarka.inject.android.fragment.compiler.FragmentFactoryGenerator
import me.tatarka.inject.android.fragment.compiler.GENERATE_FRAGMENT_FACTORY
import me.tatarka.inject.compiler.FailedToGenerateException
import me.tatarka.inject.compiler.INJECT
import me.tatarka.inject.compiler.ksp.KSAstProvider
import me.tatarka.inject.compiler.ksp.getSymbolsWithClassAnnotation

class FragmentFactoryProcessor(
    private val codeGenerator: CodeGenerator,
    override val logger: KSPLogger,
) : SymbolProcessor, KSAstProvider {

    override lateinit var resolver: Resolver

    private val generator = FragmentFactoryGenerator(this)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        this.resolver = resolver

        val injectFragments = resolver.getSymbolsWithClassAnnotation(INJECT.packageName, INJECT.simpleName)
            .map { it.toAstClass() }
            .filter { it.isInstanceOf(FRAGMENT.packageName, FRAGMENT.simpleName) }
            .toList()

        for (element in resolver.getSymbolsWithClassAnnotation(
            GENERATE_FRAGMENT_FACTORY.packageName, GENERATE_FRAGMENT_FACTORY.simpleName
        )) {
            val astClass = element.toAstClass()
            try {
                val file = generator.generate(astClass, injectFragments)
                file.writeTo(codeGenerator, aggregating = true)
            } catch (e: FailedToGenerateException) {
                error(e.message.orEmpty(), e.element)
                // Continue so we can see all errors
            }
        }
        return emptyList()
    }
}

class FragmentFactoryProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FragmentFactoryProcessor(environment.codeGenerator, environment.logger)
    }
}