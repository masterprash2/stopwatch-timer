package life.circles.stopwatch.application.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import life.circles.stopwatch.application.TimerWatchApplication
import life.circles.stopwatch.data.TimerModel
import life.circles.stopwatch.gateway.NotificationService
import life.circles.stopwatch.gateway.PreferenceStore
import life.circles.stopwatch.gateway.ResourceProvider
import life.circles.stopwatch.gateway.SystemTimeGateway
import life.circles.stopwatch.gateway.impl.AndroidPreferenceStore
import life.circles.stopwatch.gateway.impl.AndroidResourceProvider
import life.circles.stopwatch.gateway.impl.AndroidSystemTimeGateway
import life.circles.stopwatch.gateway.impl.NotificationServiceImpl
import life.circles.stopwatch.home.TimeRunner

@Module
class TimerAppModule {

    @AppScope
    @Provides
    fun preferenceStore(preferenceStore: AndroidPreferenceStore): PreferenceStore {
        return preferenceStore
    }


    @AppScope
    @Provides
    fun notificationService(notificationServiceImpl: NotificationServiceImpl): NotificationService {
        return notificationServiceImpl
    }


    @AppScope
    @Provides
    fun sharedPreferences(application: TimerWatchApplication): SharedPreferences {
        return application.getSharedPreferences("appPreference", Context.MODE_PRIVATE)
    }

    @Provides
    fun context(application: TimerWatchApplication): Context {
        return application
    }

    @AppScope
    @Provides
    fun systemTimeGateway(context: Context): SystemTimeGateway {
        return AndroidSystemTimeGateway(
            context

        )
    }

    @AppScope
    @Provides
    fun resourceProvider(resourceProvider: AndroidResourceProvider): ResourceProvider {
        return resourceProvider
    }

    @Provides
    fun timeRunner(timerModel: TimerModel, systemTimeGateway: SystemTimeGateway): TimeRunner {
        return TimeRunner(timerModel, Schedulers.computation(), AndroidSchedulers.mainThread(), systemTimeGateway)
    }


}