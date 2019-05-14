package life.circles.stopwatch.home.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import life.circles.stopwatch.formDialog.ui.StopWatchFormFragment

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun timerFormFragment(): StopWatchFormFragment


}