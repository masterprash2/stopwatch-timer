package life.circles.stopwatch.home

import androidx.appcompat.app.AppCompatActivity
import life.circles.stopwatch.formDialog.ui.StopWatchFormFragment
import javax.inject.Inject


class StopWatchNavigationImpl @Inject constructor(private val activity: AppCompatActivity) : StopWatchNavigation {

    override fun showPrompt() {
        StopWatchFormFragment.showDialog(activity.supportFragmentManager)
    }

}