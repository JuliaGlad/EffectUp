package myapplication.android.mindall.presentation.plans.trackers.nightTrackers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.common.delegate.MainAdapter
import myapplication.android.mindall.common.delegateItems.adviseBox.AdviseBoxDelegate
import myapplication.android.mindall.common.delegateItems.adviseBox.AdviseBoxDelegateItem
import myapplication.android.mindall.common.delegateItems.adviseBox.AdviseBoxModel
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegate
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegateItem
import myapplication.android.mindall.common.delegateItems.tracker.TrackerDelegate
import myapplication.android.mindall.common.delegateItems.tracker.TrackerDelegateItem
import myapplication.android.mindall.common.delegateItems.tracker.TrackerModel
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.databinding.FragmentNightBinding
import myapplication.android.mindall.presentation.plans.trackers.nightTrackers.model.TrackersPresModel
import java.util.Calendar
import java.util.Random

class NightFragment : Fragment() {
    private lateinit var binding: FragmentNightBinding
    private val viewModel: NightViewModel by viewModels()
    private var yearId: String? = null
    private lateinit var year: String
    private val mainAdapter = MainAdapter()
    private val trackers = mutableListOf<DelegateItem>()
    private var monthId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        year = getCurrentYear().toString()
        viewModel.getYearId(year)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNightBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setupObserves()
    }

    private fun initAdapter() {
        binding.taskRecyclerView.adapter = mainAdapter
        mainAdapter.addDelegate(TrackerDelegate())
        mainAdapter.addDelegate(FabDelegate())
        mainAdapter.addDelegate(AdviseBoxDelegate())
    }

    private fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)

        return year
    }

    private fun setupObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.titleYear.text = year
                    binding.titleMonth.text = Calendar.getInstance().get(Calendar.YEAR).toString()
                    initRecycler(it.data)
                    binding.loading.loadingLayout.visibility = GONE
                }

                Status.ERROR -> {}
                Status.LOADING -> binding.loading.loadingLayout.visibility = VISIBLE
            }
        }

        viewModel.yearId.observe(viewLifecycleOwner) {
            if (it != null) {
                yearId = it
            }
        }

        viewModel.monthId.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getMonthTrackers(yearId.toString(), monthId.toString())
            }
        }

        viewModel.isNew.observe(viewLifecycleOwner) {
            if (yearId == null) {
                yearId = generateId()
                monthId = generateId()
            } else {
                monthId = generateId()
                viewModel.setSuccessStateWithEmptyList()
            }
        }
    }

    private fun initRecycler(data: List<TrackersPresModel>?) {
        if (data != null) {
            trackers.add(
                AdviseBoxDelegateItem(
                    AdviseBoxModel(
                        1,
                        getString(R.string.track_the_quality_of_your_sleep)
                    )
                )
            )
            if (data.isNotEmpty()) {
                for (i in data) {
                    trackers.add(data.indexOf(i), i.id, i.date, i.value)
                }
            }

        }
    }

    private fun generateId(): String {
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        return "@$id"
    }
}

private fun MutableList<DelegateItem>.add(index: Int, id: String, date: String, value: String) {
    this.add(TrackerDelegateItem(TrackerModel(
        index,
        date,
        value,
        object : ButtonClickListener{
            override fun onClick() {

            }
        }
    )))
}
