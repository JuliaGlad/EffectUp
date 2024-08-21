package myapplication.android.mindall.presentation.dialog.verifyEmail

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogVerifyEmailBinding

class VerifyEmailDialogFragment(val email: String, val password: String) : DialogFragment() {
    private val viewModel: VerifyEmailViewModel by viewModels()
    private lateinit var binding: DialogVerifyEmailBinding
    private lateinit var listener: DialogDismissedListener
    private var isVerified = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogVerifyEmailBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.userEmail.text = email

        setupObserves()

        binding.buttonCancel.setOnClickListener { dismiss() }

        binding.buttonDone.setOnClickListener {
            viewModel.verify(email, password)
        }

        return builder.setView(binding.getRoot()).create()
    }

    fun onDismissListener(listener: DialogDismissedListener) {
        this.listener = listener
    }

    private fun setupObserves() {
        viewModel.isVerified.observe(this){
            if (it == true) {
                isVerified = it
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (isVerified) {
            listener.handleDialogClose(null)
        }
    }
}