package myapplication.android.mindall.presentation.profile.edit

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.USER_NAME
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.databinding.FragmentProfileEditBinding

class ProfileEditFragment : Fragment() {

    private val viewModel: ProfileEditViewModel by viewModels()
    private lateinit var binding: FragmentProfileEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getUserData()
        binding = FragmentProfileEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserves()
        initSaveButton()
        initBackButton()
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            activity?.finish()
        }
    }

    private fun initSaveButton(){
        binding.buttonSave.setOnClickListener{
            val name = binding.editLayoutNamer.text.toString()
            val phone = binding.editLayoutPhoneNumber.text.toString()
            val status = binding.editLayoutStatus.text.toString()
            viewModel.updateData(name, phone, status)
        }
    }

    private fun setupObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.loading.loadingLayout.visibility = GONE

                    val editData = it.data

                    binding.editLayoutNamer.setText(editData?.name)
                    binding.editLayoutEmail.setText(editData?.email)
                    binding.editLayoutPhoneNumber.setText(editData?.phone)
                    binding.editLayoutStatus.setText(editData?.status)

                }

                Status.ERROR -> {

                }

                Status.LOADING -> {
                    binding.loading.loadingLayout.visibility = VISIBLE
                }
            }

        }

       viewModel.isSaved.observe(viewLifecycleOwner){
           it?.let{
               val intent: Intent = Intent().putExtra(USER_NAME, it)
               activity?.apply {
                   setResult(RESULT_OK, intent)
                   finish()
               }
           }
       }
    }
}