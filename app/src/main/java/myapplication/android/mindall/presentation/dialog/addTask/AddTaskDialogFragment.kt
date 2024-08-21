package myapplication.android.mindall.presentation.dialog.addTask

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.GREEN
import myapplication.android.mindall.common.Constants.Companion.RED
import myapplication.android.mindall.common.Constants.Companion.YELLOW
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogAddDailyTaskBinding
import java.util.Random

class AddTaskDialogFragment(
    private val dailyPlanId: String,
    private val data: String,
    private val isNew: Boolean?
) : DialogFragment() {

    private val viewModel: AddTaskViewModel by viewModels()
    private lateinit var binding: DialogAddDailyTaskBinding
    private lateinit var listener: DialogDismissedListener
    private var isAdded = false
    private var task : String? = null
    private var taskId: String? = null
    private var flag: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddDailyTaskBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        initMenu()

        binding.buttonAdd.setOnClickListener {
           taskId = generateTaskId()
            if (binding.editLayoutTitle.getText()?.isNotEmpty() == true) {
                task = binding.editLayoutTitle.text.toString()
                flag?.let {
                    viewModel.addTask(isNew, data, dailyPlanId, taskId.toString(), task.toString(), it)
                }
            }
        }

        setupObserves()

        return builder.setView(binding.root).create()
    }

    private fun initMenu() {
        binding.imageButton.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.task_flag_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.menu.getItem(0).setOnMenuItemClickListener { item: MenuItem? ->
                chooseFlag(item)
                false
            }
            popupMenu.menu.getItem(1).setOnMenuItemClickListener { item: MenuItem? ->
                chooseFlag(item)
                false
            }
            popupMenu.menu.getItem(2).setOnMenuItemClickListener { item: MenuItem? ->
                chooseFlag(item)
                false
            }
        }
    }

    private fun chooseFlag(item: MenuItem?) {
        binding.imageButton.setImageDrawable(item?.icon)
        flag = when (item?.title) {
            getString(R.string.urgent) -> RED
            getString(R.string.not_very_urgent) -> YELLOW
            getString(R.string.not_urgent) -> GREEN
            else -> null
        }
    }

    private fun generateTaskId(): String {
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        return "@$id"
    }

    private fun setupObserves() {
        viewModel.isAdded.observe(this) {
            if (it) {
                isAdded = true
                dismiss()
            }
        }
    }

    fun onDismissListener(listener: DialogDismissedListener) {
        this.listener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (isAdded) {
            val bundle = Bundle().apply {
                putString(Constants.TASK, task)
                putString(Constants.FLAG, flag)
                putString(Constants.ID, taskId)
            }
            listener.handleDialogClose(bundle)
        }
    }
}