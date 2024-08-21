package myapplication.android.mindall.presentation.plans.trackers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.R
import myapplication.android.mindall.common.delegate.MainAdapter
import myapplication.android.mindall.common.delegateItems.buttonWithIconAndText.ButtonWithTextAndIconDelegate
import myapplication.android.mindall.common.delegateItems.buttonWithIconAndText.ButtonWithTextAndIconDelegateItem
import myapplication.android.mindall.common.delegateItems.buttonWithIconAndText.ButtonWithTextAndIconDelegateModel
import myapplication.android.mindall.common.delegateItems.statistic.StatisticsDelegate
import myapplication.android.mindall.common.delegateItems.statistic.StatisticsDelegateItem
import myapplication.android.mindall.common.delegateItems.statistic.StatisticsModel
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.databinding.FragmentTrackersBinding

class TrackersFragment : Fragment() {

    private lateinit var binding: FragmentTrackersBinding
    private val mainAdapter = MainAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        initRecycler()
    }

    private fun initRecycler() {
        val items = listOf(
            StatisticsDelegateItem(StatisticsModel(
                1,
                object : ButtonClickListener {
                    override fun onClick() {

                    }
                }
            )),
            ButtonWithTextAndIconDelegateItem(
                ButtonWithTextAndIconDelegateModel(
                    2,
                    getString(R.string.sleep),
                    getString(R.string.sleep_tracker_desc),
                    R.drawable.ic_sleep,
                    object : ButtonClickListener {
                        override fun onClick() {
                            navigate(R.id.action_trackersFragment_to_nightFragment)
                        }
                    }
                )
            ),
            ButtonWithTextAndIconDelegateItem(
                ButtonWithTextAndIconDelegateModel(
                    3,
                    getString(R.string.mood),
                    getString(R.string.mood_tracker_desc),
                    R.drawable.ic_mood,
                    object : ButtonClickListener {
                        override fun onClick() {
                            navigate(R.id.action_trackersFragment_to_moodTrackersFragment)
                        }
                    }
                )
            ),
            ButtonWithTextAndIconDelegateItem(
                ButtonWithTextAndIconDelegateModel(
                    4,
                    getString(R.string.habits),
                    getString(R.string.habits_tracker_desc),
                    R.drawable.ic_habits,
                    object : ButtonClickListener {
                        override fun onClick() {
                            navigate(R.id.action_trackersFragment_to_habitTrackersFragment)
                        }
                    }
                )
            )
        )
        mainAdapter.submitList(items)
    }

    private fun navigate(action: Int) {
        NavHostFragment.findNavController(this)
            .navigate(action)
    }

    private fun setAdapter() {
        mainAdapter.addDelegate(StatisticsDelegate())
        mainAdapter.addDelegate(ButtonWithTextAndIconDelegate())
        binding.recyclerView.adapter = mainAdapter
    }

}