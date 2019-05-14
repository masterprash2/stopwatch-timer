package life.circles.stopwatch.gateway.impl

import android.content.Context
import life.circles.stopwatch.R
import life.circles.stopwatch.gateway.ResourceProvider
import javax.inject.Inject

class AndroidResourceProvider @Inject constructor(private val context: Context) : ResourceProvider {

    override fun getStringButtonStartAgain(): String {
        return context.getString(R.string.button_start_again)
    }

    override fun getStringButtonStart(): String {
        return context.getString(R.string.button_start)
    }


}
