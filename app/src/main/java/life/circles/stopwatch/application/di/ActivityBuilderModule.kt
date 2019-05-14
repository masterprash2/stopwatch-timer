package life.circles.stopwatch.application.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import life.circles.stopwatch.home.di.FragmentBuilderModule
import life.circles.stopwatch.home.di.MainActivityModule
import life.circles.stopwatch.home.ui.MainActivity

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class, MainActivityModule::class])
    internal abstract fun stopWatchActivity(): MainActivity


}