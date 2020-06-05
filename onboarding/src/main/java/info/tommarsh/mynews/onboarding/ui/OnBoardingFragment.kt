package info.tommarsh.mynews.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import info.tommarsh.mynews.core.util.ViewModelFactory
import info.tommarsh.mynews.core.util.makeGone
import info.tommarsh.mynews.core.util.makeVisible
import info.tommarsh.mynews.onboarding.R
import info.tommarsh.mynews.onboarding.databinding.FragmentOnboardingBinding
import info.tommarsh.mynews.onboarding.model.Action
import info.tommarsh.mynews.onboarding.model.Event
import info.tommarsh.mynews.onboarding.ui.adapter.OnBoardingAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
internal class OnBoardingFragment(private val viewModelFactory: ViewModelFactory) : Fragment() {

    private val viewModel by navGraphViewModels<OnBoardingViewModel>(R.id.onboarding_nav_graph) { viewModelFactory }

    private val adapter = OnBoardingAdapter()

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        setUpNextButton()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val key = arguments?.getString(EXTRA_KEY) ?: "onboarding_sources"

        lifecycleScope.launchWhenResumed {
            viewModel.events.collect { event ->
                binding.onboardingProgress.makeGone()
                when (event) {
                    is Event.Loading -> binding.onboardingProgress.makeVisible()
                    is Event.Error -> showErrorToast()
                    is Event.Fetched -> {
                        playAnimation(event.model.animation)
                        adapter.submitList(event.model.choices)
                    }
                }
            }
        }

        viewModel.postAction(Action.FetchOnBoardingModel(key))
    }

    private fun playAnimation(fileName: String) {
        binding.onboardingAnimation.setAnimation(fileName)
        binding.onboardingAnimation.playAnimation()
    }

    private fun setUpNextButton() {
        binding.onboardingNextButton.setOnClickListener {
        }
    }

    private fun setUpRecyclerView() {
        binding.onboardingRecyclerView.adapter = adapter
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Error OnBoarding.", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_KEY = "extra_key"
    }
}