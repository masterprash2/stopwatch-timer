package life.circles.stopwatch.formDialog.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import dagger.android.support.DaggerDialogFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import life.circles.stopwatch.R
import life.circles.stopwatch.databinding.FragmentDialogFormBinding
import life.circles.stopwatch.formDialog.controller.StopWatchFormController
import javax.inject.Inject


class StopWatchFormFragment : DaggerDialogFragment() {

    var binding: FragmentDialogFormBinding? = null;
    @Inject
    lateinit var controller: StopWatchFormController

    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val onCreateDialog = super.onCreateDialog(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, DialogFragment.STYLE_NORMAL)
        onCreateDialog.setTitle(R.string.form_dialog_title)
        return onCreateDialog

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDialogFormBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.viewModel = controller.viewModel()
        binding?.controller = controller;
        binding?.inputField?.addTextChangedListener {
            value -> controller.onMinutesTextChanged(value!!);
        }
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(observeToastMessages())
        compositeDisposable.add(observeDismissCommand())
    }

    private fun observeDismissCommand(): Disposable {
        return controller.viewModel().observeDismissCommand().filter { value ->
            value
        }.subscribe {
            dismiss()
        }
    }

    private fun observeToastMessages(): Disposable {
        return controller.viewModel().observeToastMessages().subscribe { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        compositeDisposable.dispose()
        super.onStop()
    }

    override fun onDestroyView() {
        binding?.viewModel = null
        binding?.controller = null;
        binding = null
        super.onDestroyView()
    }


    companion object {

        fun showDialog(fragmentManager: FragmentManager) {
            val fragment = StopWatchFormFragment()
            fragment.show(fragmentManager, "formFragment")
        }
    }


}