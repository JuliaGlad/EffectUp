package myapplication.android.mindall.presentation.plans.basicPlans.monthly.monthPlanDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.GOAL
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.MONTH
import myapplication.android.mindall.common.Constants.Companion.MONTH_ID
import myapplication.android.mindall.common.Constants.Companion.YEAR
import myapplication.android.mindall.common.Constants.Companion.YEAR_ID
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.common.delegate.MainAdapter
import myapplication.android.mindall.common.delegateItems.checkBox.CheckBoxDelegate
import myapplication.android.mindall.common.delegateItems.checkBox.CheckBoxDelegateItem
import myapplication.android.mindall.common.delegateItems.checkBox.CheckBoxModel
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegate
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegateItem
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabModel
import myapplication.android.mindall.common.delegateItems.goalItem.GoalDelegate
import myapplication.android.mindall.common.delegateItems.goalItem.GoalDelegateItem
import myapplication.android.mindall.common.delegateItems.goalItem.GoalModel
import myapplication.android.mindall.common.listeners.ButtonBundleClickListener
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.FragmentMonthPlanDetailsBinding
import myapplication.android.mindall.presentation.dialog.addGoalDialog.AddMonthlyGoalDialogFragment
import myapplication.android.mindall.presentation.dialog.addMonthlyTaskDialog.AddMonthlyTaskDialogFragment
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.TaskDailyPlanModel
import myapplication.android.mindall.presentation.plans.basicPlans.monthly.model.GoalPresModel

class MonthPlanDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMonthPlanDetailsBinding
    private val viewModel: MonthPlanDetailsViewModel by viewModels()
    private val goalsAdapter = MainAdapter()
    private val taskAdapter = MainAdapter()

    private val tasksList = mutableListOf<DelegateItem>()
    private val goalsList = mutableListOf<DelegateItem>()

    private val completedTasks = mutableListOf<String>()
    private val notCompletedTasks = mutableListOf<String>()

    private var isNew: Boolean? = false
    private lateinit var year: String
    private lateinit var yearId: String
    private lateinit var month: String
    private lateinit var monthId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNew = arguments?.getBoolean(IS_NEW)
        year = getStringArgument(YEAR)
        yearId = getStringArgument(YEAR_ID)
        month = getStringArgument(MONTH)
        monthId = getStringArgument(MONTH_ID)
    }

    private fun getStringArgument(key: String) = arguments?.getString(key).toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonthPlanDetailsBinding.inflate(layoutInflater)
        viewModel.getTasksAndGoals(yearId, monthId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtonBack()
        handleBackButtonPressed()
        setupObserves()
        initAdapters()
    }

    private fun setupObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    val tasks = data?.tasks
                    val goals = data?.goals

                    initTasksRecycler(tasks)
                    initGoalsRecycler(goals)

                    binding.loading.root.visibility = GONE
                }

                Status.ERROR -> {}
                Status.LOADING -> binding.loading.root.visibility = VISIBLE
            }
        }

        viewModel.completedTasksUpdated.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.updateStatusNotCompleted(yearId, monthId, notCompletedTasks)
            }
        }

        viewModel.notCompletedTasksUpdated.observe(viewLifecycleOwner) {
            if (it) {
                NavHostFragment.findNavController(this).popBackStack()
            }
        }
    }

    private fun initGoalsRecycler(goals: List<GoalPresModel>?) {
        if (goals != null) {
            for (i in goals) {
                goalsList.addGoal(
                    goals.indexOf(i),
                    i.goal
                )
            }
        }
        goalsList.addFab(goalsList.size) { setupAddGoalDialog() }

        goalsAdapter.submitList(goalsList)
    }

    private fun initTasksRecycler(tasks: List<TaskDailyPlanModel>?) {
        if (tasks != null) {
            for (i in tasks) {
                tasksList.addTask(
                    tasks.indexOf(i),
                    i.id,
                    i.title,
                    i.flag,
                    i.isCompleted,
                    completedTasks,
                    notCompletedTasks
                )
            }
        }

        tasksList.addFab(tasksList.size) { setupAddTaskDialog() }

        taskAdapter.submitList(tasksList)
    }

    private fun setupAddTaskDialog() {
        val dialogFragment =
            isNew?.let { AddMonthlyTaskDialogFragment(it, yearId, year, monthId, month) }
        activity?.supportFragmentManager?.let {
            dialogFragment?.show(
                it,
                "ADD_MONTHLY_TASK_DIALOG"
            )
        }
        dialogFragment?.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                tasksList.removeLast()
                taskAdapter.notifyItemRemoved(tasksList.size)
                bundle?.let {
                    bundle.getString(Constants.ID)?.let { taskId ->
                        tasksList.addTask(
                            tasksList.size + 1,
                            taskId,
                            bundle.getString(Constants.TASK),
                            bundle.getString(Constants.FLAG),
                            false,
                            completedTasks,
                            notCompletedTasks
                        )
                    }
                }
                taskAdapter.notifyItemInserted(tasksList.size - 1)
                tasksList.addFab(tasksList.size) { setupAddTaskDialog() }
                taskAdapter.notifyItemInserted(tasksList.size - 1)
            }
        })
    }

    private fun setupAddGoalDialog() {
        val dialogFragment =
            isNew?.let { AddMonthlyGoalDialogFragment(it, yearId, year, monthId, month) }
        activity?.supportFragmentManager?.let {
            dialogFragment?.show(
                it,
                "ADD_MONTHLY_GOAL_DIALOG"
            )
        }
        dialogFragment?.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                goalsList.removeLast()
                goalsAdapter.notifyItemRemoved(goalsList.size)
                bundle?.let { goalsList.addGoal(goalsList.size, bundle.getString(GOAL).toString()) }
                goalsAdapter.notifyItemInserted(goalsList.size - 1)
                goalsList.addFab(goalsList.size) { setupAddGoalDialog() }
                goalsAdapter.notifyItemInserted(goalsList.size - 1)
            }
        })
    }

    private fun initAdapters() {
        taskAdapter.addDelegate(CheckBoxDelegate())
        taskAdapter.addDelegate(FabDelegate())

        goalsAdapter.addDelegate(FabDelegate())
        goalsAdapter.addDelegate(GoalDelegate())

        binding.goalRecyclerView.adapter = goalsAdapter
        binding.tasksRecyclerView.adapter = taskAdapter
    }

    private fun handleBackButtonPressed() {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.updateStatusCompleted(yearId, monthId, completedTasks)
            }
        })
    }

    private fun initButtonBack() {
        binding.buttonBack.setOnClickListener {
            viewModel.updateStatusCompleted(yearId, monthId, completedTasks)
        }
    }
}

private fun MutableList<DelegateItem>.addGoal(index: Int, goal: String) {
    this.add(GoalDelegateItem(GoalModel(index, goal)))
}

private fun MutableList<DelegateItem>.addFab(index: Int, function: () -> Unit) {
    this.add(
        FabDelegateItem(
            FabModel(index,
                object : ButtonClickListener {
                    override fun onClick() {
                        function.invoke()
                    }
                })
        )
    )
}

private fun MutableList<DelegateItem>.addTask(
    index: Int,
    id: String,
    title: String?,
    flag: String?,
    completed: Boolean?,
    completedTasks: MutableList<String>,
    notCompletedTasks: MutableList<String>,
) {
    this.add(
        CheckBoxDelegateItem(
            CheckBoxModel(index, id, title, flag, completed,
                object : ButtonClickListener {
                    override fun onClick() {

                    }
                },
                object : ButtonBundleClickListener {
                    override fun onClick(bundle: Bundle) {
                        val isCompleted = bundle.getBoolean(Constants.IS_COMPLETE)
                        val taskId = bundle.getString(Constants.ID)
                        if (isCompleted) {
                            completedTasks.add(taskId.toString())
                        } else {
                            notCompletedTasks.add(taskId.toString())
                        }
                    }

                })
        )
    )
}