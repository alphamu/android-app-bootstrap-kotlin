package com.alimuzaffar.blank.di.component

import android.app.Application
import com.alimuzaffar.blank.App
import com.alimuzaffar.blank.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, ActivityModule::class, FragmentModule::class, NetModule::class, ViewModelModule::class, RepositoryModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }

    fun inject(app: App)
} 