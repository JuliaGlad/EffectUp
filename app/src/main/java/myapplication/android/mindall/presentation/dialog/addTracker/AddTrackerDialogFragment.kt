package myapplication.android.mindall.presentation.dialog.addTracker

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.MOOD_TRACKER
import myapplication.android.mindall.common.Constants.Companion.NIGHT_TRACKER
import myapplication.android.mindall.common.Constants.Companion.VALUE
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.common.recyclerView.trackerItem.TrackerAdapter
import myapplication.android.mindall.common.recyclerView.trackerItem.TrackerModel
import myapplication.android.mindall.databinding.DialogAddTrackerBinding
import java.util.Random

class AddTrackerDialogFragment(
    val type: String,
    val isNew: Boolean,
    val yearId: String,
    val year: String,
    val monthId: String,
    val month: String,
    val date: String
) : DialogFragment() {

    private lateinit var binding: DialogAddTrackerBinding
    private val viewModel: AddTrackerViewModel by viewModels()
    private var isAdded = false
    private lateinit var listener: DialogDismissedListener
    private lateinit var value: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddTrackerBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.titleDate.text = date

        initRecycler()
        initTitle()
        initIcon()
        initCancelButton()
        initAddButton()
        setupObserves()

        return builder.setView(binding.root).create()
    }

    private fun initIcon() {
        if (type == NIGHT_TRACKER){
            binding.iconShield.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_sleep, context?.theme))
        } else if (type == MOOD_TRACKER){
            binding.iconShield.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_mood, context?.theme))
        }
    }

    private fun initTitle() {
        if (type == NIGHT_TRACKER){
            binding.title.text = getString(R.string.choose_the_quality_of_your_sleep_on)
        } else if (type == MOOD_TRACKER){
            binding.title.text = getString(R.string.choose_the_quality_of_your_mood_on)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (isAdded){
            listener.handleDialogClose(Bundle().apply {
                putString(VALUE, value)
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
            viewModel.addTracker(type, isNew, yearId, year, monthId, month, trackerId, date, value)
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
        val qualities = resources.getStringArray(R.array.qualities)
        val items = mutableListOf<TrackerModel>()
        initQualities(items, qualities, adapter)
        binding.recyclerView.adapter = adapter
        adapter.submitList(items)
    }

    private fun initQualities(
        items: MutableList<TrackerModel>,
        qualities: Array<String>,
        adapter: TrackerAdapter
    ) {
        addQualities(items, 1, qualities[0], R.drawable.ic_very_good, adapter)
        addQualities(items, 2, qualities[1], R.drawable.ic_good, adapter)
        addQualities(items, 3, qualities[2], R.drawable.ic_neutral, adapter)
        addQualities(items, 4, qualities[3], R.drawable.ic_bad, adapter)
    }

    private fun addQualities(
        items: MutableList<TrackerModel>,
        id: Int,
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
                        value = valueText
                        for (i in items) {
                            i.isChosen = i.id == id
                            adapter.notifyItemChanged(items.indexOf(i))
                        }
                    }
                }
            )
        )
    }

}