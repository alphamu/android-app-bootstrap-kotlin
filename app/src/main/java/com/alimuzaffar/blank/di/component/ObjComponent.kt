package com.alimuzaffar.blank.di.component


import com.alimuzaffar.blank.di.scope.UserScope
import dagger.Component

@UserScope
@Component(dependencies = arrayOf(AppComponent::class))
interface ObjComponent {
    fun inject(obj: Any)
}
