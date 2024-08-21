package myapplication.android.mindall.presentation.dialog.logout

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogLogoutBinding

class LogoutDialogFragment : DialogFragment() {

    private val viewModel: LogoutViewModel by viewModels()
    private lateinit var binding: DialogLogoutBinding
    private lateinit var listener: DialogDismissedListener
    private var isLogout = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogLogoutBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        setupObserves()

        binding.buttonCancel.setOnClickListener { dismiss() }

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            dismiss()
        }

        return builder.setView(binding.getRoot()).create()

    }

    fun onDismissListener(listener: DialogDismissedListener) {
        this.listener = listener
    }

    private fun setupObserves() {
        viewModel.isLogOut.observe(this) {
            if (it == true) {
                isLogout = it
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.handleDialogClose(null)
    }
}