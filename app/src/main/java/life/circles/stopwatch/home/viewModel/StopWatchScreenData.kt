package life.circles.stopwatch.home.viewModel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import life.circles.stopwatch.data.TimerModel
import life.circles.stopwatch.home.StopWatchNavigation
import javax.inject.Inject

class StopWatchScreenData @Inject constructor(private val navigation: StopWatchNavigation) {

    val timerString = ObservableField<String>()
    val isExpired = ObservableBoolean()
    val isTimerRunning = ObservableBoolean()
    val buttonText = ObservableField<String>()
    var timerState = TimerModel.TimerState.IDLE

    fun showInputPrompt() {
        navigation.showPrompt()
    }

}