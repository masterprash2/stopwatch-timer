package life.circles.stopwatch.formDialog.interactor

import life.circles.stopwatch.data.TimerModel
import javax.inject.Inject

class StartTimerInteractor @Inject constructor(private val timerModel: TimerModel) {

    fun startTimer(duration: String): Pair<Boolean, String?> {
        val validate = MinutesValidator.validate(duration)
        return if (!validate.first) {
            Pair(validate.first, validate.second)
        } else {
            val minutes = validate.third
            timerModel.start(minutes.toLong());
            Pair(true, null)
        }
    }

}