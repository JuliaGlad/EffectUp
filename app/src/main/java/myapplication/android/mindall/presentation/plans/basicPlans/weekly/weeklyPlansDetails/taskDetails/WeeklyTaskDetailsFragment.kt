package myapplication.android.mindall.presentation.plans.basicPlans.weekly.weeklyPlansDetails.taskDetails

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
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.DAYS
import myapplication.android.mindall.common.Constants.Companion.DAY_ID
import myapplication.android.mindall.common.Constants.Companion.DAY_OF_WEEK
import myapplication.android.mindall.common.Constants.Companion.DURATION
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.WEEKLY_PLAN_ID
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.common.delegate.MainAdapter
import myapplication.android.mindall.common.delegateItems.checkBox.CheckBoxDelegate
import myapplication.android.mindall.common.delegateItems.checkBox.CheckBoxDelegateItem
import myapplication.android.mindall.common.delegateItems.checkBox.CheckBoxModel
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegate
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegateItem
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabModel
import myapplication.android.mindall.common.listeners.ButtonBundleClickListener
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.FragmentWeeklyTaskDetailsBinding
import myapplication.android.mindall.presentation.dialog.addWeeklyTaskiDialog.AddWeeklyTaskDialogFragment
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.TaskDailyPlanModel
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.model.DayOfWeekPresModel

class WeeklyTaskDetailsFragment : Fragment() {
    private lateinit var binding: FragmentWeeklyTaskDetailsBinding
    private val viewModel: WeeklyTaskDetailsViewModel by viewModels()
    private var dayOfWeek: String? = null
    private val tasksList = mutableListOf<DelegateItem>()
    private val completedTasks = mutableListOf<String>()
    private val notCompletedTasks = mutableListOf<String>()
    private var date: String? = null
    private var week: String? = null
    private val adapter = MainAdapter()
    private lateinit var days: List<DayOfWeekPresModel>
    private var isNew: Boolean? = null
    private var weeklyPlanId: String? = null
    private var dayId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dayId = arguments?.getString(DAY_ID)
        weeklyPlanId = arguments?.getString(WEEKLY_PLAN_ID)
        isNew = arguments?.getBoolean(IS_NEW)
        date = arguments?.getString(DATA)
        week = arguments?.getString(DURATION)
        dayOfWeek = arguments?.getString(DAY_OF_WEEK)
        days = Gson().fromJson(
            arguments?.getString(DAYS),
            object : TypeToken<List<DayOfWeekPresModel>>() {}.type
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeeklyTaskDetailsBinding.inflate(layoutInflater)
        viewModel.getTasks(weeklyPlanId, dayId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackButton()
        handleBackButtonPressed()
        setupObserves()
    }

    private fun handleBackButtonPressed() {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                weeklyPlanId?.let { weeklyId ->
                    dayId?.let { dayId ->
                        viewModel.updateStatusCompleted(weeklyId, dayId, completedTasks)
                    }
                }
            }
        })
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            weeklyPlanId?.let { weeklyId ->
                dayId?.let { dayId ->
                    viewModel.updateStatusCompleted(weeklyId, dayId, completedTasks)
                }
            }

        }
    }

    private fun setupObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val tasks = it.data
                    initHeader()
                    initRecycler(tasks)
                    binding.loading.root.visibility = GONE
                }

                Status.ERROR -> {}
                Status.LOADING -> binding.loading.root.visibility = VISIBLE
            }
        }

        viewModel.completedTasksUpdated.observe(viewLifecycleOwner) {
            if (it) {
                weeklyPlanId?.let { weeklyId ->
                    dayId?.let { dayId ->
                        viewModel.updateStatusNotCompleted(weeklyId, dayId, notCompletedTasks)
                    }
                }
            }
        }

        viewModel.notCompletedTasksUpdated.observe(viewLifecycleOwner){
            if (it){
                NavHostFragment.findNavController(this).popBackStack()
            }
        }
    }

    private fun initRecycler(tasks: List<TaskDailyPlanModel>?) {
        adapter.addDelegate(CheckBoxDelegate())
        adapter.addDelegate(FabDelegate())

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

        tasksList.addTaskFab(tasksList.size) { setupAddTaskDialog() }

        binding.taskRecyclerView.adapter = adapter
        adapter.submitList(tasksList)
    }

    private fun setupAddTaskDialog() {
        val dialogFragment = AddWeeklyTaskDialogFragment(
            isNew,
            week,
            days,
            weeklyPlanId,
            dayId
        )

        activity?.supportFragmentManager?.let { dialogFragment.show(it, "ADD_TASK_DIALOG") }

        dialogFragment.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                tasksList.removeLast()
                adapter.notifyItemRemoved(tasksList.size)
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
                adapter.notifyItemInserted(tasksList.size - 1)
                tasksList.addTaskFab(tasksList.size) { setupAddTaskDialog() }
                adapter.notifyItemInserted(tasksList.size - 1)
            }
        })
    }

    private fun initHeader() {
        binding.titleCustom.text = date
    }
}

private fun MutableList<DelegateItem>.addTaskFab(index: Int, function: () -> Unit) {
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
