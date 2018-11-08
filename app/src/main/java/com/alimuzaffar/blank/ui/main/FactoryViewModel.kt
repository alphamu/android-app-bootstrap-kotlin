package com.alimuzaffar.blank.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/***
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
@Singleton
class FactoryViewModel @Inject
constructor(private val creators: Map<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("unknown model class $modelClass")
        }
        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}
