package myapplication.android.mindall.presentation.dialog.addHabit

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.common.Constants.Companion.HABIT
import myapplication.android.mindall.common.Constants.Companion.HABIT_ID
import myapplication.android.mindall.common.Constants.Companion.VALUE
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogAddHabitBinding
import java.util.Random

class AddHabitDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAddHabitBinding
    private val viewModel: AddHabitViewModel by viewModels()
    private var isAdded = false
    private lateinit var habit: String
    private lateinit var habitId: String
    private lateinit var listener: DialogDismissedListener
    private lateinit var value: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddHabitBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        initCancelButton()
        initAddButton()
        setupObserves()

        return builder.setView(binding.root).create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (isAdded){
            listener.handleDialogClose(Bundle().apply {
                putString(HABIT, habit)
            })
        }
    }

    fun onDismissListener(listener: DialogDismissedListener) {
        this.listener = listener
    }

    private fun setupObserves() {
        viewModel.isAdded.observe(this) {
            if (it) {
                isAdded = true
                dismiss()
            }
        }
    }

    private fun initAddButton() {
        binding.buttonAdd.setOnClickListener {
            habitId = generateId()
            habit = binding.editLayoutTitle.text.toString()
            viewModel.addHabit(habitId, habit)
        }
    }


    private fun generateId(): String {
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        return "@$id"
    }

    private fun initCancelButton() {
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

}