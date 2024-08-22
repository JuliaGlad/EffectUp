package myapplication.android.mindall.presentation.plans.trackers.habiitTrackers.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.HABIT
import myapplication.android.mindall.common.Constants.Companion.HABIT_ID
import myapplication.android.mindall.common.Constants.Companion.IS_COMPLETE
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.MONTH
import myapplication.android.mindall.common.Constants.Companion.MONTH_ID
import myapplication.android.mindall.common.Constants.Companion.YEAR
import myapplication.android.mindall.common.Constants.Companion.YEAR_ID
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.common.delegate.MainAdapter
import myapplication.android.mindall.common.delegateItems.adviseBox.AdviseBoxDelegate
import myapplication.android.mindall.common.delegateItems.adviseBox.AdviseBoxDelegateItem
import myapplication.android.mindall.common.delegateItems.adviseBox.AdviseBoxModel
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegate
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegateItem
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabModel
import myapplication.android.mindall.common.delegateItems.habitTrackerDaily.HabitTrackerDelegate
import myapplication.android.mindall.common.delegateItems.habitTrackerDaily.HabitTrackerDelegateItem
import myapplication.android.mindall.common.delegateItems.habitTrackerDaily.HabitTrackerModel
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.FragmentHabitTrackersBinding
import myapplication.android.mindall.presentation.dialog.addHabitTracker.AddHabitTrackerDialogFragment
import myapplication.android.mindall.presentation.dialog.alreadyHaveTrackerDialog.AlreadyHaveTrackerDialogFragment
import myapplication.android.mindall.presentation.plans.trackers.habiitTrackers.model.HabitPresModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HabitTrackersFragment : Fragment() {

    private lateinit var binding: FragmentHabitTrackersBinding
    private lateinit var habit: String
    private lateinit var habitId: String
    private lateinit var yearId: String
    private lateinit var monthId: String
    private lateinit var month: String
    private lateinit var year: String
    private var isNew: Boolean? = false
    private lateinit var date : String
    private var isAdded: Boolean = false
    private val items = mutableListOf<DelegateItem>()
    private val mainAdapter = MainAdapter()
    private val viewModel: HabitTrackersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitTrackersBinding.inflate(layoutInflater)
        getArgs()
        date = getCurrentDate()
        if (!isNew!!){
            viewModel.getTrackers(yearId, monthId, habitId)
        } else {
            viewModel.setStateSuccess()
        }
        return binding.root
    }

    private fun getCurrentDate(): String {
        val date =
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
        return date.toString()
    }

    private fun getArgs() {
        habit = getArgument(HABIT)
        habitId = getArgument(HABIT_ID)
        yearId = getArgument(YEAR_ID)
        year = getArgument(YEAR)
        monthId = getArgument(MONTH_ID)
        month = getArgument(MONTH)
        isNew = arguments?.getBoolean(IS_NEW)
    }

    private fun getArgument(const: String): String {
        return arguments?.getString(const).toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTitle()
        initBackButton()
        setupObserves()
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_habitTrackersFragment_to_chooseHabitFragment)
        }
    }

    private fun setupObserves() {
        viewModel.state.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    it.data?.let { models -> initRecycler(models) }
                    binding.loading.root.visibility = GONE
                }
                Status.ERROR -> {}
                Status.LOADING -> binding.loading.root.visibility = VISIBLE
            }
        }
    }

    private fun initRecycler(models: List<HabitPresModel>) {
        setAdapter()
        items.add(AdviseBoxDelegateItem(AdviseBoxModel(0, getString(R.string.track_your_progress_improve_and_go_towards_your_goal))))
        for (i in models){
            if (i.date == date){
                isAdded = true
            }
            addTracker(items.size, i.date, i.isComplete)
        }
        addFab(items.size)
        mainAdapter.submitList(items)
    }

    private fun addFab(id: Int) {
        items.add(FabDelegateItem(FabModel(
            id,
            object : ButtonClickListener {
                override fun onClick() {
                    if (!isAdded) {
                        setupAddHabitTracker()
                    } else {
                        setupAlreadyHaveTracker()
                    }
                }
            }
        )))
    }

    private fun setupAlreadyHaveTracker() {
        val dialogFragment = AlreadyHaveTrackerDialogFragment()
        activity?.supportFragmentManager?.let {
            dialogFragment.show(
                it,
                "ALREADY_HAVE_TRACKER_DIALOG"
            )
        }
    }

    private fun setupAddHabitTracker() {
        val dialogFragment =
            isNew?.let { AddHabitTrackerDialogFragment(it, habitId, yearId, year, monthId, month, date) }
        activity?.supportFragmentManager?.let {
            dialogFragment?.show(
                it,
                "Add habit tracker dialog"
            )
        }
        dialogFragment?.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                isAdded = true
                items.removeLast()
                mainAdapter.notifyItemRemoved(items.size - 1)
                bundle?.getBoolean(IS_COMPLETE)
                    ?.let { addTracker(items.size, date, it) }
                addFab(items.size)
            }
        })
    }

    private fun addTracker(index: Int, date: String, complete: Boolean) {
        items.add(HabitTrackerDelegateItem(HabitTrackerModel(
            index,
            date,
            complete,
            object : ButtonClickListener{
                override fun onClick() {

                }
            }
        )))
    }

    private fun setAdapter() {
        binding.habitRecyclerView.adapter = mainAdapter
        mainAdapter.addDelegate(FabDelegate())
        mainAdapter.addDelegate(AdviseBoxDelegate())
        mainAdapter.addDelegate(HabitTrackerDelegate())
    }

    private fun initTitle() {
        binding.titleHabit.text = habit
    }

}