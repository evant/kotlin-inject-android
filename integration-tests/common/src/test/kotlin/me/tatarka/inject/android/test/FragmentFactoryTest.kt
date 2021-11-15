package me.tatarka.inject.android.test

import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.tatarka.inject.android.Fragment1
import me.tatarka.inject.android.Fragment2
import me.tatarka.inject.android.TestFragmentFactoryActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentFactoryTest {
    @Test
    fun constructs_injected_fragments() {
        val scenario = launchActivity<TestFragmentFactoryActivity>()

        scenario.onActivity {
            it.supportFragmentManager.commit {
                add<Fragment1>(tag = "fragment1")
                add<Fragment2>(tag = "fragment2")
            }
        }
    }
}