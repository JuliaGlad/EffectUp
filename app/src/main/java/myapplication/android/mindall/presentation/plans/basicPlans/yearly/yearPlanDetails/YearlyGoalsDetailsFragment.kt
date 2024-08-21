package myapplication.android.mindall.presentation.plans.basicPlans.yearly.yearPlanDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.EVENT
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.TASK
import myapplication.android.mindall.common.Constants.Companion.YEAR
import myapplication.android.mindall.common.Constants.Companion.YEAR_ID
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.common.delegate.MainAdapter
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegate
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegateItem
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabModel
import myapplication.android.mindall.common.delegateItems.goalItem.GoalDelegate
import myapplication.android.mindall.common.delegateItems.goalItem.GoalDelegateItem
import myapplication.android.mindall.common.delegateItems.goalItem.GoalModel
import myapplication.android.mindall.common.delegateItems.importantDate.ImportantDateDelegate
import myapplication.android.mindall.common.delegateItems.importantDate.ImportantDateDelegateItem
import myapplication.android.mindall.common.delegateItems.importantDate.ImportantDateModel
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.FragmentYearlyGoalsDetailsBinding
import myapplication.android.mindall.presentation.dialog.addDateDialog.AddDateDialogFragment
import myapplication.android.mindall.presentation.dialog.addYearlyGoalDialog.AddYearlyGoalDialogFragment
import myapplication.android.mindall.presentation.plans.basicPlans.monthly.model.GoalPresModel
import myapplication.android.mindall.presentation.plans.basicPlans.yearly.models.DatesModel
import kotlin.properties.Delegates

class YearlyGoalsDetailsFragment : Fragment() {
    private lateinit var binding: FragmentYearlyGoalsDetailsBinding
    private val viewModel: YearlyGoalsDetailsViewModel by viewModels()

    private val goalsList = mutableListOf<DelegateItem>()
    private val datesList = mutableListOf<DelegateItem>()

    private val goalAdapter = MainAdapter()
    private val datesAdapter = MainAdapter()

    private lateinit var yearId: String
    private var isNew by Delegates.notNull<Boolean>()
    private lateinit var year: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        yearId = getStringArgument(YEAR_ID)
        year = getStringArgument(YEAR)
        isNew = arguments?.getBoolean(IS_NEW) == true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYearlyGoalsDetailsBinding.inflate(layoutInflater)
        viewModel.getDatesAndGoals(yearId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGoalsAdapter()
        initDatesAdapter()
        initBackButton()
        setupObserves()
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    private fun initDatesAdapter() {
        datesAdapter.addDelegate(ImportantDateDelegate())
        datesAdapter.addDelegate(FabDelegate())

        binding.datesRecyclerView.adapter = datesAdapter
    }

    private fun initGoalsAdapter() {
        goalAdapter.addDelegate(FabDelegate())
        goalAdapter.addDelegate(GoalDelegate())

        binding.goalsRecyclerView.adapter = goalAdapter
    }

    private fun setupObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    val goals = data?.goals
                    val dates = data?.dates

                    binding.titleCustom.text = year

                    initGoalsRecycler(goals)
                    initDatesRecycler(dates)

                    binding.loading.root.visibility = GONE
                }

                Status.ERROR -> {}
                Status.LOADING -> binding.loading.root.visibility = VISIBLE
            }
        }
    }

    private fun initDatesRecycler(dates: MutableList<DatesModel>?) {
        if (dates != null) {
            for (i in dates) {
                datesList.addDates(dates.indexOf(i), i.date, i.task)
            }
        }

        datesList.addFab(datesList.size) { setupAddDateDialog() }
        datesAdapter.submitList(datesList)
    }

    private fun setupAddGoalDialog() {
        val dialogFragment = AddYearlyGoalDialogFragment(isNew, yearId, year)
        activity?.supportFragmentManager?.let {
            dialogFragment.show(
                it,
                "ADD_YEARLY_GOAL_DIALOG_FRAGMENT"
            )
        }
        dialogFragment.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                goalsList.removeLast()
                goalAdapter.notifyItemRemoved(goalsList.size)
                bundle?.let {
                    goalsList.addGoal(
                        goalsList.size,
                        bundle.getString(Constants.GOAL).toString()
                    )
                }
                goalAdapter.notifyItemInserted(goalsList.size - 1)
                goalsList.addFab(goalsList.size) { setupAddGoalDialog() }
                goalAdapter.notifyItemInserted(goalsList.size - 1)
            }
        })
    }

    private fun setupAddDateDialog() {
        val dialogFragment = AddDateDialogFragment(isNew, yearId, year)
        activity?.supportFragmentManager?.let {
            dialogFragment.show(
                it,
                "ADD_YEARLY_DATE_DIALOG_FRAGMENT"
            )
        }
        dialogFragment.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                datesList.removeLast()
                datesAdapter.notifyItemRemoved(datesList.size)
                bundle?.let {
                    datesList.addDates(
                        datesList.size,
                        bundle.getString(DATA).toString(),
                        bundle.getString(EVENT).toString()
                    )
                }
                datesAdapter.notifyItemInserted(datesList.size - 1)
                datesList.addFab(datesList.size) { setupAddDateDialog() }
                datesAdapter.notifyItemInserted(datesList.size - 1)
            }
        })
    }

    private fun initGoalsRecycler(goals: MutableList<GoalPresModel>?) {
        if (goals != null) {
            for (i in goals) {
                goalsList.addGoal(goals.indexOf(i), i.goal)
            }
        }

        goalsList.addFab(goalsList.size) { setupAddGoalDialog() }
        goalAdapter.submitList(goalsList)
    }

    private fun getStringArgument(key: String) = arguments?.getString(key).toString()
}

private fun MutableList<DelegateItem>.addGoal(index: Int, goal: String) {
    this.add(
        GoalDelegateItem(
            GoalModel(
                index,
                goal
            )
        )
    )
}

private fun MutableList<DelegateItem>.addFab(size: Int, setupDialog: () -> Unit) {
    this.add(
        FabDelegateItem(
            FabModel(
                size,
                object : ButtonClickListener {
                    override fun onClick() {
                        setupDialog.invoke()
                    }
                })
        )
    )
}

private fun MutableList<DelegateItem>.addDates(index: Int, date: String, task: String) {
    this.add(
        ImportantDateDelegateItem(
            ImportantDateModel(
                index, date, task
            )
        )
    )
}
