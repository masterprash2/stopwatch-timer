package life.circles.stopwatch.application.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import life.circles.stopwatch.application.TimerWatchApplication

@AppScope
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ActivityBuilderModule::class,
        TimerAppModule::class,
        ReceiverBuilderModule::class]
)
interface TimerAppComponent : AndroidInjector<TimerWatchApplication> {


    @Component.Factory
    interface Factory : AndroidInjector.Factory<TimerWatchApplication> {

    }

}