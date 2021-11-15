package me.tatarka.inject.android.fragment.compiler.kapt

import me.tatarka.inject.android.annotations.GenerateFragmentFactory
import me.tatarka.inject.android.fragment.compiler.FRAGMENT
import me.tatarka.inject.android.fragment.compiler.FragmentFactoryGenerator
import me.tatarka.inject.android.fragment.compiler.GENERATE_FRAGMENT_FACTORY
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.compiler.FailedToGenerateException
import me.tatarka.inject.compiler.INJECT
import me.tatarka.inject.compiler.kapt.ModelAstProvider
import me.tatarka.inject.compiler.kapt.ModelOutputProvider
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class FragmentFactoryCompiler : AbstractProcessor(), ModelAstProvider, ModelOutputProvider {

    private lateinit var filer: Filer
    override lateinit var env: ProcessingEnvironment
    private lateinit var generator: FragmentFactoryGenerator

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        this.env = processingEnv
        this.filer = processingEnv.filer
        generator = FragmentFactoryGenerator(this)
    }

    override fun process(elements: Set<TypeElement>, env: RoundEnvironment): Boolean {
        if (elements.isEmpty()) {
            return false
        }

        val injectFragments =
            env.getElementsAnnotatedWith(Inject::class.java).filterIsInstance<TypeElement>().map { it.toAstClass() }
                .filter { it.isInstanceOf(FRAGMENT.packageName, FRAGMENT.simpleName) }

        for (element in env.getElementsAnnotatedWith(GenerateFragmentFactory::class.java)) {
            if (element !is TypeElement) continue

            val astClass = element.toAstClass()

            try {
                val file = generator.generate(astClass, injectFragments)
                file.writeTo(filer)
            } catch (e: FailedToGenerateException) {
                error(e.message.orEmpty(), e.element)
            }
        }

        return false
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedOptions(): Set<String> = setOf()

    override fun getSupportedAnnotationTypes(): Set<String> =
        setOf(INJECT.canonicalName, GENERATE_FRAGMENT_FACTORY.canonicalName)
}