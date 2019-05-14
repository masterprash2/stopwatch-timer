package life.circles.stopwatch.formDialog.viewModel

import javax.inject.Inject

class StopWatchFormPresenter @Inject constructor(val viewModel: StopWatchFormViewModel) {


    fun showMessage(message: String) {
        viewModel.showMessage(message)
    }

    fun dismiss() {
        viewModel.dismissDialog()
    }

    fun updateDisplayMinutes(value: Int) {
        if(value != 0)
            viewModel.minutes.set(value.toString())
        else {
            viewModel.minutes.set("")
            viewModel.minutes.notifyChange()
        }
    }

}