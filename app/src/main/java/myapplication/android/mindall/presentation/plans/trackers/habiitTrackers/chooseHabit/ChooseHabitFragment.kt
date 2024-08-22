package myapplication.android.mindall.presentation.plans.trackers.habiitTrackers.chooseHabit

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.HABIT
import myapplication.android.mindall.common.Constants.Companion.HABIT_ID
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.MONTH
import myapplication.android.mindall.common.Constants.Companion.MONTH_ID
import myapplication.android.mindall.common.Constants.Companion.YEAR
import myapplication.android.mindall.common.Constants.Companion.YEAR_ID
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.common.delegate.MainAdapter
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegate
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegateItem
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabModel
import myapplication.android.mindall.common.delegateItems.habit.HabitDelegate
import myapplication.android.mindall.common.delegateItems.habit.HabitDelegateItem
import myapplication.android.mindall.common.delegateItems.habit.HabitModel
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.FragmentChooseHabitBinding
import myapplication.android.mindall.presentation.dialog.addHabit.AddHabitDialogFragment
import myapplication.android.mindall.presentation.plans.trackers.habiitTrackers.model.HabitTitleAndIDModel
import java.util.Calendar
import java.util.Random

class ChooseHabitFragment : Fragment() {

    private lateinit var binding: FragmentChooseHabitBinding
    private lateinit var year: String
    private var yearId: String? = null
    private lateinit var month: String
    private var isNew: Boolean = false
    private var monthId: String? = null
    private lateinit var habit: String
    private val items = mutableListOf<DelegateItem>()
    private var habitId: String? = null
    private val adapter = MainAdapter()
    private val viewModel: ChooseHabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        year = getCurrentYear()
        month = getCurrentMonth()
        viewModel.getHabits()
    }

    private fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val monthNumber = calendar.get(Calendar.MONTH)

        return getMonthByNumber(monthNumber)
    }

    private fun getMonthByNumber(monthNumber: Int) =
        resources.getStringArray(R.array.month)[monthNumber]

    private fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)

        return year.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseHabitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        initTitle()
        initInfoBox()
        initBackButton()
        setupObserves()
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    private fun initInfoBox() {
        binding.infoBox.body.text =
            getString(R.string.track_your_progress_improve_and_go_towards_your_goal)
    }

    private fun initTitle() {
        binding.titleYear.text = year
        binding.titleMonth.text = month
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
        adapter.addDelegate(HabitDelegate())
        adapter.addDelegate(FabDelegate())
    }

    private fun setupObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    initRecycler(it.data)
                    binding.loading.root.visibility = GONE
                }

                Status.ERROR -> {}
                Status.LOADING -> binding.loading.root.visibility = VISIBLE
            }
        }

        viewModel.isNew.observe(viewLifecycleOwner) {
            if (it != null) {
                checkId()
            }
        }

        viewModel.habitId.observe(viewLifecycleOwner) {
            if (it != null) {
                habitId = it
                viewModel.getYearId(habitId.toString(), year)
            }
        }

        viewModel.yearId.observe(viewLifecycleOwner) {
            if (it != null) {
                yearId = it
                viewModel.getMonthId(yearId.toString(), habitId.toString(), month)
            }
        }

        viewModel.monthId.observe(viewLifecycleOwner) {
            if (it != null) {
                monthId = it
                navigate()
            }
        }
    }

    private fun checkId() {
        isNew = true
        if (habitId == null) {
            habitId = generateId()
            yearId = generateId()
            monthId = generateId()

            navigate()
        } else if (yearId == null) {
            yearId = generateId()
            monthId = generateId()
            navigate()
        } else if (monthId == null) {
            monthId = generateId()
            navigate()
        }
    }

    private fun navigate() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_chooseHabitFragment_to_habitTrackersFragment, Bundle().apply {
                putString(HABIT, habit)
                putString(HABIT_ID, habitId)
                putString(YEAR_ID, yearId)
                putBoolean(IS_NEW, isNew)
                putString(YEAR, year)
                putString(MONTH_ID, monthId)
                putString(MONTH, month)
            })
    }

    private fun generateId(): String {
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        return "@$id"
    }

    private fun initRecycler(habitTitleAndIDModels: List<HabitTitleAndIDModel>?) {
        if (habitTitleAndIDModels != null) {
            for (i in habitTitleAndIDModels) {
                addHabitDelegateItem(i.habit)
            }
            addFabDelegateItem()
            adapter.submitList(items)
        }
    }

    private fun addFabDelegateItem() {
        items.add(
            FabDelegateItem(
                FabModel(
                    items.size,
                    object : ButtonClickListener {
                        override fun onClick() {
                            setupAddHabitDialog()
                        }
                    })
            )
        )
    }

    private fun setupAddHabitDialog() {
        val dialogFragment = AddHabitDialogFragment()
        activity?.supportFragmentManager?.let { dialogFragment.show(it, "ADD_HABIT_DIALOG") }
        dialogFragment.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                items.removeLast()
                adapter.notifyItemRemoved(items.size - 1)
                addHabitDelegateItem(bundle?.getString(HABIT).toString())
                addFabDelegateItem()
                adapter.notifyItemRangeInserted(items.size - 2, 2)
            }
        })
    }

    private fun addHabitDelegateItem(
        habitTitle: String
    ) {
        items.add(
            HabitDelegateItem(
                HabitModel(
                    items.size,
                    habitTitle,
                    object : ButtonClickListener {
                        override fun onClick() {
                            habit = habitTitle
                            viewModel.getHabitId(habit)
                        }
                    })
            )
        )
    }
}