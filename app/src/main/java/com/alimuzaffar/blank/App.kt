package com.alimuzaffar.blank

import android.app.Activity
import android.app.Application
import android.content.Context
import com.alimuzaffar.blank.di.component.AppComponent
import com.alimuzaffar.blank.di.component.DaggerAppComponent
import com.alimuzaffar.blank.di.component.DaggerObjComponent
import com.alimuzaffar.blank.di.component.ObjComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    val baseUrl: String?
        get() = null

    override fun onCreate() {
        super.onCreate()
        app = this
        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build()
        appComponent!!.inject(this)

        // Use this to inject plain old objects if needed
        objComponent = DaggerObjComponent.builder().appComponent(appComponent).build()
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    companion object {
        private var app: App? = null
        private var appComponent: AppComponent? = null
        var objComponent: ObjComponent? = null
            private set

        val context: Context
            get() = app!!.baseContext
    }
}
