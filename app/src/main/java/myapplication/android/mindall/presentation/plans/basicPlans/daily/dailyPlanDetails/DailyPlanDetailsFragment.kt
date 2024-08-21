package myapplication.android.mindall.presentation.plans.basicPlans.daily.dailyPlanDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.DAILY_PLAN_DATE
import myapplication.android.mindall.common.Constants.Companion.DURATION
import myapplication.android.mindall.common.Constants.Companion.FLAG
import myapplication.android.mindall.common.Constants.Companion.ID
import myapplication.android.mindall.common.Constants.Companion.IS_COMPLETE
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.IS_NOTIFICATION_ON
import myapplication.android.mindall.common.Constants.Companion.TASK
import myapplication.android.mindall.common.Constants.Companion.TITLE
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.common.delegate.MainAdapter
import myapplication.android.mindall.common.delegateItems.checkBox.CheckBoxDelegate
import myapplication.android.mindall.common.delegateItems.checkBox.CheckBoxDelegateItem
import myapplication.android.mindall.common.delegateItems.checkBox.CheckBoxModel
import myapplication.android.mindall.common.delegateItems.daySchedule.DayScheduleDelegate
import myapplication.android.mindall.common.delegateItems.daySchedule.DayScheduleDelegateItem
import myapplication.android.mindall.common.delegateItems.daySchedule.DayScheduleModel
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegate
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabDelegateItem
import myapplication.android.mindall.common.delegateItems.floatingActionButton.FabModel
import myapplication.android.mindall.common.listeners.ButtonBundleClickListener
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.FragmentPlanDetailsBinding
import myapplication.android.mindall.presentation.dialog.addTask.AddTaskDialogFragment
import myapplication.android.mindall.presentation.plans.basicPlans.daily.DailyPlansActivity
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.ScheduleDayModel
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.TaskDailyPlanModel

class DailyPlanDetailsFragment : Fragment() {
    private val viewModel: DailyPlanDetailsViewModel by viewModels()
    private lateinit var binding: FragmentPlanDetailsBinding
    private val taskAdapter: MainAdapter = MainAdapter()
    private val scheduleAdapter: MainAdapter = MainAdapter()

    private lateinit var launcher: ActivityResultLauncher<Intent>

    private var tasks : MutableList<DelegateItem> = mutableListOf()
    private var schedules : MutableList<DelegateItem> = mutableListOf()
    private var completedTasks : MutableList<String> = mutableListOf()
    private var notCompletedTasks : MutableList<String> = mutableListOf()

    private lateinit var chosenDate: String
    private lateinit var chosenDateId: String

    private var isNew: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chosenDateId = getArgument(Constants.DAILY_PLAN_ID)
        chosenDate = getArgument(DAILY_PLAN_DATE)
        isNew = getBooleanArgument()
        initLauncher()
    }

    private fun initLauncher() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if ((result.resultCode == Activity.RESULT_OK) and (result.data != null)) {
                    schedules.removeLast()
                    scheduleAdapter.notifyItemRemoved(schedules.size)
                    schedules.add(DayScheduleDelegateItem(DayScheduleModel(
                        schedules.size,
                        result.data?.getStringExtra(TITLE),
                        result.data?.getStringExtra(DURATION),
                        result.data?.getBooleanExtra(IS_NOTIFICATION_ON, true),
                        object : ButtonClickListener{
                            override fun onClick() {

                            }
                        }
                    )))
                    addScheduleFab()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getDailyPlanData(chosenDateId)
        binding = FragmentPlanDetailsBinding.inflate(layoutInflater)
        binding.titleCustom.text = chosenDate
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserves()
        initBackButton()
        handleBackButtonPressed()
        initTaskAdapter()
        initScheduleAdapter()
    }

    private fun handleBackButtonPressed() {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.changeCompletedTaskStatus(chosenDateId, completedTasks)
            }
        })
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            viewModel.changeCompletedTaskStatus(chosenDateId, completedTasks)
        }
    }

    private fun initScheduleAdapter() {
        binding.dayScheduleRecyclerView.adapter = scheduleAdapter
        scheduleAdapter.addDelegate(DayScheduleDelegate())
        scheduleAdapter.addDelegate(FabDelegate())
    }

    private fun initTaskAdapter() {
        binding.taskRecyclerView.adapter = taskAdapter
        taskAdapter.addDelegate(FabDelegate())
        taskAdapter.addDelegate(CheckBoxDelegate())
    }

    private fun setupObserves() {
        viewModel.statusCompletedUpdated.observe(viewLifecycleOwner){
            if (it){
                viewModel.changeNotCompletedTaskStatus(chosenDateId, notCompletedTasks)
            }
        }

        viewModel.statusNotCompletedUpdated.observe(viewLifecycleOwner){
            if (it){
                NavHostFragment.findNavController(this).popBackStack()
            }
        }

        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state.status){
                Status.SUCCESS -> {
                    state.data?.let {
                        initTasksRecycler(it.tasks)
                        initSchedulesRecycler(it.schedules)
                        binding.loading.loadingLayout.visibility = GONE
                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> binding.loading.loadingLayout.visibility = VISIBLE
            }
        }
    }

    private fun initSchedulesRecycler(schedules: List<ScheduleDayModel>) {
        for (i in schedules){
            this.schedules.add(
                DayScheduleDelegateItem(DayScheduleModel(
                    schedules.indexOf(i),
                    i.task,
                    i.duration,
                    i.isNotificationOn,
                    object : ButtonClickListener{
                        override fun onClick() {

                        }
                    }
                ))
            )
        }
        addScheduleFab()
        scheduleAdapter.submitList(this.schedules)
    }

    private fun addScheduleFab() {
        schedules.add(
            FabDelegateItem(FabModel(
                schedules.size + 1,
                object : ButtonClickListener {
                    override fun onClick() {
                        Log.i("Chose date", chosenDateId)
                        (activity as DailyPlansActivity).openAddScheduleActivity(chosenDateId, launcher)
                    }
                }
            ))
        )
    }

    private fun initTasksRecycler(tasks: List<TaskDailyPlanModel>) {
        for (i in tasks){
            addTaskDelegate(
                tasks.indexOf(i),
                i.id,
                i.title,
                i.flag,
                i.isCompleted
            )
        }
        addTaskFab()
        taskAdapter.submitList(this.tasks)
    }

    private fun addTaskFab() {
        this.tasks.add(
            FabDelegateItem(FabModel(
                this.tasks.size + 1,
                object : ButtonClickListener {
                    override fun onClick() {
                        setupAddTaskDialog()
                    }
                }
            ))
        )
    }

    private fun addTaskDelegate(
        index: Int?,
        id: String?,
        title: String?,
        flag: String?,
        isComplete: Boolean?
    ) {
        this.tasks.add(
            CheckBoxDelegateItem(CheckBoxModel(
                index,
                id,
                title,
                flag,
                isComplete,
                object : ButtonClickListener{
                    override fun onClick() {

                    }
                },
                object : ButtonBundleClickListener {
                    override fun onClick(bundle: Bundle) {
                        val isCompleted = bundle.getBoolean(IS_COMPLETE)
                        val taskId = bundle.getString(ID)
                        if (isCompleted){
                            completedTasks.add(taskId.toString())
                        } else {
                            notCompletedTasks.add(taskId.toString())
                        }
                    }
                }
            ))
        )
    }

    private fun setupAddTaskDialog() {
        val dialogFragment = AddTaskDialogFragment(chosenDateId, chosenDate, isNew)

        activity?.supportFragmentManager?.let {dialogFragment.show(it, "ADD_TASK_DIALOG")}

        dialogFragment.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                tasks.removeLast()
                taskAdapter.notifyItemRemoved(tasks.size)
                bundle?.let {
                    addTaskDelegate(
                        tasks.size + 1,
                        bundle.getString(ID),
                        bundle.getString(TASK),
                        bundle.getString(FLAG),
                        false
                    )
                }
                taskAdapter.notifyItemInserted(tasks.size - 1)
                addTaskFab()
                taskAdapter.notifyItemInserted(tasks.size - 1)
            }
        })
    }

    private fun getArgument(constant: String) = arguments?.getString(constant).toString()

    private fun getBooleanArgument() = arguments?.getBoolean(IS_NEW)
}