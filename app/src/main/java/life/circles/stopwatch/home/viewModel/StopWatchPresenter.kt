package life.circles.stopwatch.home.viewModel

import life.circles.stopwatch.data.TimerModel
import life.circles.stopwatch.gateway.ResourceProvider
import javax.inject.Inject

class StopWatchPresenter @Inject constructor(
    val screenData: StopWatchScreenData,
    private val resourceProvider: ResourceProvider
) {

    private fun reset() {
        screenData.buttonText.set(resourceProvider.getStringButtonStart())
        screenData.isExpired.set(false)
        screenData.timerString.set("00:00")
        screenData.isTimerRunning.set(false)
    }

    fun startTimer() {
        screenData.isTimerRunning.set(true)
        screenData.isExpired.set(false)
    }

    fun updateTime(timer: String) {
        screenData.timerString.set(timer)
    }

    private fun timerExpired() {
        screenData.isTimerRunning.set(false)
        screenData.isExpired.set(true)
        screenData.buttonText.set(resourceProvider.getStringButtonStartAgain())
        screenData.timerString.set("00:00")
    }

    fun takeUserInput() {
        screenData.showInputPrompt();
    }

    fun updateState(state: TimerModel.TimerState) {
        when (state) {
            TimerModel.TimerState.IDLE -> reset()
            TimerModel.TimerState.RUNNING -> startTimer()
            TimerModel.TimerState.EXPIRED -> timerExpired()
        }
        screenData.timerState = state
    }
}