package life.circles.stopwatch.home.viewModel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import life.circles.stopwatch.data.TimerModel
import life.circles.stopwatch.home.TimeRunner
import javax.inject.Inject

class StopWatchViewModel @Inject constructor(
    private val presenter: StopWatchPresenter,
    val timerModel: TimerModel,
    private val timeRunner: TimeRunner
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var timeRunnerDisposable : Disposable? = null

    fun setup() {
        compositeDisposable.add(timerModel.observeTimerState().subscribe {
            presenter.updateState(it)
            if(it == TimerModel.TimerState.RUNNING) {
                observeTimeRunner()
            }
            else {
                timeRunnerDisposable?.dispose()
            }
        })
    }


    fun beginStopWatch() {
        if(viewData().isExpired.get())
            timerModel.reset()
        else
            presenter.takeUserInput();
    }

    fun viewData(): StopWatchScreenData {
        return presenter.screenData
    }

    private fun observeTimeRunner() {
        timeRunnerDisposable?.dispose()
        timeRunnerDisposable = timeRunner.observeTimeChanges().subscribe {
            presenter.updateTime(it)
        }
        compositeDisposable.add(timeRunnerDisposable!!)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}