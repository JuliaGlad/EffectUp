package myapplication.android.mindall.presentation.dialog.updatePassword

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogUpdatePasswordBinding

class UpdatePasswordDialogFragment : DialogFragment() {

    private val viewModel : UpdatePasswordViewModel by viewModels()
    private lateinit var binding: DialogUpdatePasswordBinding
    private lateinit var listener: DialogDismissedListener
    private var isUpdated = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogUpdatePasswordBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        setupObserves()

        binding.buttonCancel.setOnClickListener {dismiss()}

        binding.buttonSend.setOnClickListener{
            val oldPassword : String = binding.editLayoutOldPassword.text.toString()
            val newPassword : String = binding.editLayoutNewPassword.text.toString()
            viewModel.updatePassword(oldPassword, newPassword)
        }

        return builder.setView(binding.getRoot()).create()
    }

    fun onDismissListener(listener: DialogDismissedListener) {
        this.listener = listener
    }

    private fun setupObserves() {
        viewModel.isUpdated.observe(this){
            if (it == true) {
                isUpdated = it
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (isUpdated){
            listener.handleDialogClose(null)
        }
    }
}