package myapplication.android.mindall.presentation.dialog.addHabitTracker

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.COMPLETE
import myapplication.android.mindall.common.Constants.Companion.INCOMPLETE
import myapplication.android.mindall.common.Constants.Companion.IS_COMPLETE
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.common.recyclerView.trackerItem.TrackerAdapter
import myapplication.android.mindall.common.recyclerView.trackerItem.TrackerModel
import myapplication.android.mindall.databinding.DialogAddTrackerBinding
import java.util.Random

class AddHabitTrackerDialogFragment(
    val isNew: Boolean,
    val habitId: String,
    val yearId: String,
    val year: String,
    val monthId: String,
    val month: String,
    val date: String
) : DialogFragment() {

    private lateinit var binding: DialogAddTrackerBinding
    private val viewModel: AddHabitTrackerViewModel by viewModels()
    private var isAdded = false
    private var isComplete = false
    private lateinit var listener: DialogDismissedListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddTrackerBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.titleDate.text = date
        Log.i("Is new, yearId, monthId", "$isNew $yearId $monthId")
        initRecycler()
        initCancelButton()
        initAddButton()
        setupObserves()

        return builder.setView(binding.root).create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (isAdded) {
            listener.handleDialogClose(Bundle().apply {
                putBoolean(IS_COMPLETE, isComplete)
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
            val trackerId = generateId()
            viewModel.addTracker(
                isNew,
                yearId,
                year,
                monthId,
                month,
                habitId,
                trackerId,
                date,
                isComplete
            )
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

    private fun initRecycler() {
        val adapter = TrackerAdapter()
        val items = mutableListOf<TrackerModel>()
        initQualities(items, adapter)
        binding.recyclerView.adapter = adapter
        adapter.submitList(items)
    }

    private fun initQualities(
        items: MutableList<TrackerModel>,
        adapter: TrackerAdapter
    ) {
        addQualities(
            items,
            items.size,
            COMPLETE,
            getString(R.string.complete),
            R.drawable.ic_complete,
            adapter
        )
        addQualities(
            items,
            items.size,
            INCOMPLETE,
            getString(R.string.incomplete),
            R.drawable.ic_incomplete,
            adapter
        )
    }

    private fun addQualities(
        items: MutableList<TrackerModel>,
        id: Int,
        value: String,
        valueText: String,
        valueIcon: Int,
        adapter: TrackerAdapter
    ) {
        items.add(
            TrackerModel(
                id,
                valueText,
                valueIcon,
                false,
                object : ButtonClickListener {
                    override fun onClick() {
                        for (i in items) {
                            i.isChosen = i.id == id
                            adapter.notifyItemChanged(items.indexOf(i))
                        }

                        isComplete = value == COMPLETE
                    }
                }
            )
        )
    }

}