package myapplication.android.mindall.presentation.profile.settings

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import myapplication.android.mindall.R
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.Constants.Companion.EMAIL
import myapplication.android.mindall.common.Constants.Companion.PASSWORD
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.databinding.FragmentSettingsBinding
import myapplication.android.mindall.presentation.dialog.deleteAccount.DeleteAccountDialogFragment
import myapplication.android.mindall.presentation.dialog.logout.LogoutDialogFragment
import myapplication.android.mindall.presentation.dialog.success.SuccessDialogFragment
import myapplication.android.mindall.presentation.dialog.updateEmail.UpdateEmailDialogFragment
import myapplication.android.mindall.presentation.dialog.updatePassword.UpdatePasswordDialogFragment
import myapplication.android.mindall.presentation.dialog.verifyEmail.VerifyEmailDialogFragment
import myapplication.android.mindall.common.recyclerView.buttonWithIcon.ButtonWithIconAdapter
import myapplication.android.mindall.common.recyclerView.buttonWithIcon.ButtonWithIconModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getUserData()
        binding = FragmentSettingsBinding.inflate(layoutInflater)
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
                    binding.userName.text = it.data?.name
                    binding.phoneNumber.text = it.data?.email

                    val adapter = ButtonWithIconAdapter()
                    initRecycler(adapter)
                }

                Status.ERROR -> {}

                Status.LOADING -> {
                    binding.loading.loadingLayout.visibility = VISIBLE
                }
            }
        }

        viewModel.isVerifyBeforeUpdate.observe(viewLifecycleOwner) {
            it?.let {
                val dialog = VerifyEmailDialogFragment(
                    it.getString(EMAIL).toString(),
                    it.getString(PASSWORD).toString()
                )
                activity?.supportFragmentManager?.let { act ->
                    dialog.show(
                        act,
                        "VERIFICATION_DIALOG"
                    )
                }
                dialog.onDismissListener(object : DialogDismissedListener {
                    override fun handleDialogClose(bundle: Bundle?) {
                        val successDialog = SuccessDialogFragment()
                        activity?.supportFragmentManager?.let { successAct ->
                            successDialog.show(
                                successAct,
                                "SUCCESS_DIALOG"
                            )
                        }
                    }
                })
            }
        }
    }

    private fun initRecycler(adapter: ButtonWithIconAdapter) {
        val items: List<ButtonWithIconModel> = listOf(
            ButtonWithIconModel(1, getString(R.string.change_password),
                R.drawable.ic_password, false,
                object : ButtonClickListener {
                    override fun onClick() {
                        val dialogFragment = UpdatePasswordDialogFragment()
                        activity?.supportFragmentManager?.let {
                            dialogFragment.show(
                                it,
                                "CHANGE_PASSWORD_DIALOG"
                            )
                        }
                        dialogFragment.onDismissListener(object : DialogDismissedListener {
                            override fun handleDialogClose(bundle: Bundle?) {
                                val successDialog = SuccessDialogFragment()
                                activity?.supportFragmentManager?.let {
                                    successDialog.show(
                                        it,
                                        "SUCCESS_DIALOG"
                                    )
                                }
                            }
                        })
                    }
                }),
            ButtonWithIconModel(2, getString(R.string.update_email), R.drawable.ic_email, false,
                object : ButtonClickListener {
                    override fun onClick() {
                        val dialogFragment = UpdateEmailDialogFragment()
                        activity?.supportFragmentManager?.let {
                            dialogFragment.show(
                                it,
                                "UPDATE_EMAIL_DIALOG"
                            )
                        }
                        dialogFragment.onDismissListener(object : DialogDismissedListener {
                            override fun handleDialogClose(bundle: Bundle?) {
                                bundle?.let {
                                    viewModel.verifyBeforeUpdate(bundle)
                                }
                            }
                        })
                    }
                }),

            ButtonWithIconModel(3, getString(R.string.logout), R.drawable.ic_logout, true,
                object : ButtonClickListener {
                    override fun onClick() {
                        val dialogFragment = LogoutDialogFragment()
                        activity?.supportFragmentManager?.let {
                            dialogFragment.show(
                                it,
                                "CHANGE_PASSWORD"
                            )
                        }
                        dialogFragment.onDismissListener(object : DialogDismissedListener {
                            override fun handleDialogClose(bundle: Bundle?) {
                                activity?.apply {
                                    setResult(RESULT_OK)
                                    finish()
                                }
                            }
                        })
                    }
                }),

            ButtonWithIconModel(3, getString(R.string.delete_account), R.drawable.ic_delete, true,
                object : ButtonClickListener {
                    override fun onClick() {
                        val dialogFragment = DeleteAccountDialogFragment()
                        activity?.supportFragmentManager?.let { dialogFragment.show(it, "DELETE_ACCOUNT_FRAGMENT") }
                        dialogFragment.onDismissListener(object : DialogDismissedListener {
                            override fun handleDialogClose(bundle: Bundle?) {
                                activity?.apply {
                                    setResult(RESULT_OK)
                                    finish()
                                }
                            }

                        })
                    }
                })
        )
        binding.recyclerView.adapter = adapter
        adapter.submitList(items)
    }


}