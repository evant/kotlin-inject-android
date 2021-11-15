package me.tatarka.inject.android.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import me.tatarka.inject.annotations.Inject

/**
 * A [FragmentFactory] that allows fragment constructor injection. You can provide the fragments to inject with
 * `@Provides @IntoMap`.
 *
 * ```
 * @Component abstract class FragmentsComponent {
 *     abstract val fragmentFactory: InjectFragmentFactory
 *
 *     val (() -> MyFragment1).myFragment1: Pair<String, () -> Fragment>
 *         @Provides @IntoMap get() = this::class.java.name to this
 *
 *     val (() -> MyFragment2).myFragment2: Pair<String, () -> Fragment>
 *         @Provides @IntoMap get() = this::class.java.name to this
 * }
 * ```
 */
@Inject
class InjectFragmentFactory(private val fragmentFactories: Map<String, () -> Fragment>) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return fragmentFactories[className]?.invoke() ?: super.instantiate(classLoader, className)
    }
}