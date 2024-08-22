package myapplication.android.mindall.presentation.plans.trackers.moodTrackers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.MOOD_TRACKER
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.common.delegate.MainAdapter
import myapplication.android.mindall.common.delegateItems.adviseBox.AdviseBoxDelegate
import myapplication.android.mindall.common.delegateItems.adviseBox.AdviseBoxDelegateItem
import myapplication.android.mindall.common.delegateItems.adviseBox.AdviseBoxModel
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegate
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegateItem
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabModel
import myapplication.android.mindall.common.delegateItems.tracker.TrackerDelegate
import myapplication.android.mindall.common.delegateItems.tracker.TrackerDelegateItem
import myapplication.android.mindall.common.delegateItems.tracker.TrackerModel
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.FragmentMoodTrackersBinding
import myapplication.android.mindall.presentation.dialog.addTracker.AddTrackerDialogFragment
import myapplication.android.mindall.presentation.dialog.alreadyHaveTrackerDialog.AlreadyHaveTrackerDialogFragment
import myapplication.android.mindall.presentation.plans.trackers.model.TrackersPresModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

class MoodTrackersFragment : Fragment() {
    private lateinit var binding: FragmentMoodTrackersBinding
    private val viewModel: MoodTrackersViewModel by viewModels()
    private var isNew = false
    private var yearId: String? = null
    private lateinit var year: String
    private val mainAdapter = MainAdapter()
    private lateinit var date: String
    private var isCurrentTrackerAdded = false
    private val trackers = mutableListOf<DelegateItem>()
    private var monthId: String? = null
    private lateinit var month: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        year = getCurrentYear().toString()
        date = getCurrentDate()
        month = getCurrentMonth()
        viewModel.getYearId(year)
    }

    private fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val monthNumber = calendar.get(Calendar.MONTH)

        return getMonthByNumber(monthNumber)
    }

    private fun getMonthByNumber(monthNumber: Int) =
        resources.getStringArray(R.array.month)[monthNumber]


    private fun getCurrentDate(): String {
        val date =
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
        return date.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoodTrackersBinding.inflate(layoutInflater)
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
                    binding.titleMonth.text = month
                    initRecycler(it.data)
                    binding.loading.loadingLayout.visibility = View.GONE
                }

                Status.ERROR -> {}
                Status.LOADING -> binding.loading.loadingLayout.visibility = View.VISIBLE
            }
        }

        viewModel.yearId.observe(viewLifecycleOwner) {
            if (it != null) {
                yearId = it
                viewModel.getMonthId(yearId.toString(), month)
            }
        }

        viewModel.monthId.observe(viewLifecycleOwner) {
            if (it != null) {
                monthId = it
                viewModel.getMonthTrackers(yearId.toString(), monthId.toString())
            }
        }

        viewModel.isNew.observe(viewLifecycleOwner) {
            if (it == true) {
                if (yearId == null) {
                    isNew = true
                    yearId = generateId()
                    monthId = generateId()
                    viewModel.setSuccessStateWithEmptyList()
                } else {
                    isNew = true
                    monthId = generateId()
                    viewModel.setSuccessStateWithEmptyList()
                }
            }
        }
    }

    private fun initRecycler(data: List<TrackersPresModel>?) {
        if (data != null) {
            trackers.add(
                AdviseBoxDelegateItem(
                    AdviseBoxModel(
                        1,
                        getString(R.string.track_your_daily_mood)
                    )
                )
            )
            if (data.isNotEmpty()) {
                for (i in data) {
                    if (i.date == date) {
                        isCurrentTrackerAdded = true
                    }
                    trackers.addTrackerItem(data.indexOf(i), i.date, i.value)
                }
            }
            trackers.addFabItem(trackers.size) {
                if (!isCurrentTrackerAdded) {
                    setupAddTrackerDialog()
                } else {
                    setupAlreadyHaveTrackerDialog()
                }
            }
            mainAdapter.submitList(trackers)
        }
    }

    private fun setupAlreadyHaveTrackerDialog() {
        val dialogFragment = AlreadyHaveTrackerDialogFragment()
        activity?.supportFragmentManager?.let {
            dialogFragment.show(
                it,
                "ALREADY_HAVE_TRACKER_DIALOG"
            )
        }
    }

    private fun setupAddTrackerDialog() {
        val dialogFragment = AddTrackerDialogFragment(
            MOOD_TRACKER,
            isNew,
            yearId.toString(),
            year,
            monthId.toString(),
            month,
            date
        )
        activity?.supportFragmentManager?.let { dialogFragment.show(it, "ADD_TRACKER_DIALOG") }
        dialogFragment.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                isCurrentTrackerAdded = true
                trackers.removeLast()
                mainAdapter.notifyItemRemoved(trackers.size - 1)
                trackers.addTrackerItem(
                    trackers.size,
                    date,
                    bundle?.getString(Constants.VALUE).toString()
                )
                trackers.addFabItem(trackers.size) {
                    setupAlreadyHaveTrackerDialog()
                }
                mainAdapter.notifyItemRangeInserted(trackers.size, 2)

            }
        })
    }

    private fun generateId(): String {
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        return "@$id"
    }
}

private fun MutableList<DelegateItem>.addFabItem(
    index: Int,
    function: () -> Unit
) {
    this.add(FabDelegateItem(FabModel(
        index,
        object : ButtonClickListener {
            override fun onClick() {
                function.invoke()
            }
        }
    )))
}

private fun MutableList<DelegateItem>.addTrackerItem(
    index: Int,
    date: String,
    value: String,
) {
    this.add(TrackerDelegateItem(TrackerModel(
        index,
        date,
        value,
        object : ButtonClickListener {
            override fun onClick() {

            }
        }
    )))
}