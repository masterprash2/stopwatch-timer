package life.circles.stopwatch.home.ui

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import life.circles.stopwatch.databinding.ActivityMainBinding
import life.circles.stopwatch.home.viewModel.StopWatchViewModel
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: StopWatchViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel.viewData()
        binding.controller = viewModel
        viewModel.setup()

    }


    override fun onDestroy() {
        binding.viewModel = null;
        binding.controller = null;
        super.onDestroy()
    }
}
