package com.alimuzaffar.blank.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alimuzaffar.blank.di.scope.ViewModelScope
import com.alimuzaffar.blank.ui.main.FactoryViewModel
import com.alimuzaffar.blank.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/***
 * This module helps inject ViewModel objects with dependencies.
 * The reason it's used is because we never call the constructor of a ViewModel directly.
 * Below we have to define a bind method with @ViewModelScope for each ViewModel we wish to Inject.
 * In order to use this, inject ViewModelProvider.Factory into your Fragment or Activity.
 * then create the new model as follows:
 * <pre>`class MyActivity extends Activity {
 *
 * ViewModelProvider.Factory viewModelFactory;
 * private MyViewModel viewModel;
 *
 *
 * protected void onCreate(Bundle savedInstanceState) {
 * ...
 * viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyViewModel.class);
 * }
 * }
`</pre> *
 */
@Module
abstract class ViewModelModule {

    // One of these needs to be defined for every ViewModel we wish to bind.
    @Binds
    @IntoMap
    @ViewModelScope(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    // No need to define any more of these.
    @Binds
    internal abstract fun bindViewModelFactory(factory: FactoryViewModel): ViewModelProvider.Factory


}
