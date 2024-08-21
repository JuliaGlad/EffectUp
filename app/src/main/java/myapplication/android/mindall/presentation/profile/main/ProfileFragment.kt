package myapplication.android.mindall.presentation.profile.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.MainActivity
import myapplication.android.mindall.R
import myapplication.android.mindall.R.drawable
import myapplication.android.mindall.R.string
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.Constants.Companion.USER_NAME
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.databinding.FragmentProfileBinding
import myapplication.android.mindall.common.recyclerView.buttonWithIcon.ButtonWithIconAdapter
import myapplication.android.mindall.common.recyclerView.buttonWithIcon.ButtonWithIconModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var settingsLauncher: ActivityResultLauncher<Intent>

    override fun onStart() {
        super.onStart()
        if (!viewModel.checkCurrentUser()) {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_profile_to_loginFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEditLauncher()
        initSettingsLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        viewModel.getUserData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserves()
    }

    private fun setupObserves() {

        viewModel.state.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.loading.loadingLayout.visibility = GONE
                    binding.userNameTitle.text = it.data?.name
                    binding.emailTitle.text = it.data?.email

                    val adapter = ButtonWithIconAdapter()
                    initRecyclerView(adapter)
                }

                Status.ERROR -> {

                }

                Status.LOADING -> {
                    binding.loading.loadingLayout.visibility = VISIBLE
                }
            }
        }
    }

    private fun initRecyclerView(adapter: ButtonWithIconAdapter) {
        val items: List<ButtonWithIconModel> = listOf(
            ButtonWithIconModel(1, getString(string.edit_profile), drawable.ic_edit,false,
                object : ButtonClickListener {
                    override fun onClick() {
                        (activity as MainActivity).openProfileEditActivity(editLauncher)
                    }
                }),
            ButtonWithIconModel(2, getString(string.appearance), drawable.ic_look_up, false,
                object : ButtonClickListener {
                    override fun onClick() {

                    }
                }),
            ButtonWithIconModel(3, getString(string.settings), drawable.ic_settings, false,
                object : ButtonClickListener {
                    override fun onClick() {
                        (activity as MainActivity).openSettingsActivity(settingsLauncher)
                    }
                })
        )
        binding.recyclerView.adapter = adapter
        adapter.submitList(items)
    }

    private fun initEditLauncher() {
        editLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            ((result.resultCode == RESULT_OK) and (result.data != null)).let {
                if (it) {
                    binding.userNameTitle.text = result.data?.getStringExtra(USER_NAME)
                }
            }
        }
    }

    private fun initSettingsLauncher() {
        settingsLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            (result.resultCode == RESULT_OK).let {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_navigation_profile_to_loginFragment)
            }
        }
    }
}