package myapplication.android.mindall.presentation.dialog.addDateDialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.DATE_ID
import myapplication.android.mindall.common.Constants.Companion.EVENT
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogAddDateBinding
import java.util.Calendar
import java.util.Random

class AddDateDialogFragment(
    val isNew: Boolean,
    val yearId: String,
    val year: String
) : DialogFragment() {
    private val viewModel: AddDateViewModel by viewModels()
    private lateinit var binding: DialogAddDateBinding
    private lateinit var listener: DialogDismissedListener
    private var isAdded = false

    private lateinit var dateId: String
    private lateinit var date: String
    private lateinit var event: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddDateBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        date = getCurrentDate()
        initCalendar()

        binding.buttonAdd.setOnClickListener {
            dateId = generateDateId()
            if (binding.editLayoutTitle.getText()?.isNotEmpty() == true) {
                event = binding.editLayoutTitle.text.toString()
                viewModel.addDate(isNew, yearId, year, dateId, date, event)
            }
        }

        setupObserves()

        return builder.setView(binding.root).create()
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()

        val month = getMonth(calendar.get(Calendar.MONTH) + 1)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$day.$month"
    }

    private fun initCalendar() {
        binding.calendar.setOnDateChangeListener { _, _, month, dayOfMonth ->
            val newMonth = getMonth(month + 1)
            date = "$dayOfMonth.$newMonth"
        }
    }

    private fun getMonth(month: Int): String {
        return if (month < 10) {
            "0$month"
        } else {
            month.toString()
        }
    }

    private fun generateDateId(): String {
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
                putString(EVENT, event)
                putString(DATA, date)
                putString(DATE_ID, dateId)
            }
            listener.handleDialogClose(bundle)
        }
    }
}