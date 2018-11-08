package com.alimuzaffar.blank.di.module

import com.alimuzaffar.blank.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Each activity you wish to inject needs to have an abstract contribute methods below.
 * <pre>`class MyActivity extends Activity implements HasFragmentInjector {
 *
 * @Inject
 * DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
 *
 *
 * protected void onCreate(Bundle savedInstanceState) {
 * // Yes, call it before calling super.
 * AndroidInjection.inject(this);
 * super.onCreate(savedInstanceState)
 * ...
 *
 * }
 *
 *
 * public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
 * return dispatchingAndroidInjector;
 * }
 * }
 *
 * class MyFragment extends Fragment {
 * ...
 *
 * public void onActivityCreated(@Nullable Bundle savedInstanceState) {
 * AndroidInjection.inject(this);
 * // If using appcompat Fragment
 * // AndroidSupportInjection.inject(this);
 * super.onActivityCreated(savedInstanceState);
 * }
 * ...
 * }
`</pre> *
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = arrayOf(FragmentModule::class))
    internal abstract fun contributeMainActivity(): MainActivity

}