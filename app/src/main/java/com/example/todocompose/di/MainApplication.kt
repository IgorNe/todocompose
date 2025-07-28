

import android.app.Application
import android.content.Context

class MainApplication:Application() {
    lateinit var applicationComponent:ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder().appModule(AppModule(this)).build()

    }
}

val Context.applicationComponent:ApplicationComponent
    get() = when(this){
        is MainApplication -> applicationComponent
        else -> this.applicationContext.applicationComponent
    }
