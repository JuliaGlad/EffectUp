package myapplication.android.mindall.presentation.dialog.addGoalDialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.common.Constants.Companion.GOAL
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogAddGoalBinding
import java.util.Random

class AddMonthlyGoalDialogFragment(
    val isNew: Boolean,
    val yearId: String,
    val year: String,
    val monthId: String,
    val month: String
) : DialogFragment() {
    private val viewModel: AddMonthlyGoalViewModel by viewModels()
    private lateinit var binding: DialogAddGoalBinding
    private lateinit var listener: DialogDismissedListener
    private var isAdded = false
    private var goalId : String? = null
    private var goal : String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddGoalBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonAdd.setOnClickListener {
            goalId = generateGoalId()
            if (binding.editLayoutTitle.getText()?.isNotEmpty() == true) {
                goal = binding.editLayoutTitle.text.toString()
                goalId?.let { goalId -> goal?.let { goal -> viewModel.addGoal(isNew, yearId, year, monthId, month, goalId, goal)}}
            }
        }

        setupObserves()

        return builder.setView(binding.root).create()
    }

    private fun generateGoalId(): String {
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
                putString(GOAL, goal)
            }
            listener.handleDialogClose(bundle)
        }
    }

}