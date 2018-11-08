package com.alimuzaffar.blank.di.module

import com.alimuzaffar.blank.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * In order to use this, make sure the Activity implements HasFragmentInjector or
 * HasSupportFragmentInjector.
 * <pre>`class MyActivity extends AppCompatActivity implements HasFragmentInjector {
 *
 * DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
 *
 *
 * protected void onCreate(Bundle savedInstanceState) {
 * // Yes, call it before calling super.
 * AndroidInjection.inject(this);
 * super.onCreate(savedInstanceState)
 * ...
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
abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMainActivityFragment(): MainFragment

}