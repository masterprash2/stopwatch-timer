package life.circles.stopwatch.formDialog.controller

import android.text.Editable
import androidx.lifecycle.ViewModel
import life.circles.stopwatch.formDialog.interactor.MinutesValidator
import life.circles.stopwatch.formDialog.interactor.StartTimerInteractor
import life.circles.stopwatch.formDialog.viewModel.StopWatchFormPresenter
import life.circles.stopwatch.formDialog.viewModel.StopWatchFormViewModel
import javax.inject.Inject

class StopWatchFormController @Inject constructor(
    private val startTimerInteractor: StartTimerInteractor,
    private val stopWatchFormPresenter: StopWatchFormPresenter
) : ViewModel() {


    fun startTimer(timer: String) {
        val value = startTimerInteractor.startTimer(timer);
        if (value.first) {
            stopWatchFormPresenter.dismiss();
        } else if (value.second != null) {
            val message = value.second!!
            stopWatchFormPresenter.showMessage(message)
        }
    }

    fun viewModel(): StopWatchFormViewModel {
        return stopWatchFormPresenter.viewModel
    }

    fun onMinutesTextChanged(value: Editable) {
        val validate = MinutesValidator.validate(value.toString());
        stopWatchFormPresenter.updateDisplayMinutes(validate.third)
    }


    override fun onCleared() {
        super.onCleared()
    }
}