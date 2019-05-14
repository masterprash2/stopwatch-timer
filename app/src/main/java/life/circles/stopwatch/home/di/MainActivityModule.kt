package life.circles.stopwatch.home.di

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import life.circles.stopwatch.home.StopWatchNavigation
import life.circles.stopwatch.home.StopWatchNavigationImpl
import life.circles.stopwatch.home.ui.MainActivity

@Module
class MainActivityModule {

    @Provides
    fun activity(mainActivity: MainActivity): AppCompatActivity {
        return mainActivity
    }

    @Provides
    fun navigation(navigation: StopWatchNavigationImpl): StopWatchNavigation {
        return navigation
    }

}
