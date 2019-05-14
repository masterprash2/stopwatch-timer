package life.circles.stopwatch.formDialog.viewModel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class StopWatchFormViewModel @Inject constructor() {

    private val messagePublisher = PublishSubject.create<String>()
    private val dismiss = BehaviorSubject.createDefault<Boolean>(false)
    val minutes = ObservableField<String>("")

    fun observeToastMessages(): Observable<String> {
        return messagePublisher.observeOn(AndroidSchedulers.mainThread())
    }

    internal fun showMessage(message: String) {
        messagePublisher.onNext(message)
    }

    fun observeDismissCommand() : Observable<Boolean> {
        return dismiss.observeOn(AndroidSchedulers.mainThread());
    }

    internal fun dismissDialog() {
        dismiss.onNext(true)
    }

}
