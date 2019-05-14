package life.circles.stopwatch.application.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import life.circles.stopwatch.receiver.StopwatchReceiver

@Module
abstract class ReceiverBuilderModule {

    @ContributesAndroidInjector()
    internal abstract fun receiver(): StopwatchReceiver



}