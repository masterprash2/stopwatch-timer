package life.circles.stopwatch.application

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import life.circles.stopwatch.application.di.DaggerTimerAppComponent

class TimerWatchApplication : DaggerApplication() {

    lateinit var androidInjector: AndroidInjector<TimerWatchApplication>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return androidInjector
    }

    override fun onCreate() {
        androidInjector = DaggerTimerAppComponent.factory().create(this)
        super.onCreate()
    }

}