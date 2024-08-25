package myapplication.android.mindall.presentation.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eftimoff.androidplayer.Player
import com.eftimoff.androidplayer.actions.property.PropertyAction
import myapplication.android.mindall.MainActivity
import myapplication.android.mindall.R
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.databinding.FragmentPlansMainBinding
import myapplication.android.mindall.common.recyclerView.buttonWithIcon.ButtonWithIconAdapter
import myapplication.android.mindall.common.recyclerView.buttonWithIcon.ButtonWithIconModel
import myapplication.android.mindall.common.recyclerView.buttonWithIconAndText.ButtonWithIconAndTextAdapter
import myapplication.android.mindall.common.recyclerView.buttonWithIconAndText.ButtonWithIconAndTextModel
import myapplication.android.mindall.presentation.dialog.needAccount.NeedAccountDialogFragment

class PlansMainFragment : Fragment() {
    private val viewModel: PlansMainViewModel by viewModels()
    private var isUserExist: Boolean = false
    private lateinit var binding: FragmentPlansMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUserExist = viewModel.checkUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlansMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val items: List<ButtonWithIconAndTextModel> = listOf(
            ButtonWithIconAndTextModel(
                1,
                getString(R.string.plans),
                getString(R.string.create_plans_for_your_cases_and_goals_for_the_next_days_weeks_month_or_years),
                R.drawable.ic_list,
                object : ButtonClickListener {
                    override fun onClick() {
                        if (isUserExist) {
                            (activity as MainActivity).openPlansActivity()
                        } else {
                            setupNeedAccountDialog()
                        }
                    }
                }),
            ButtonWithIconAndTextModel(
                2,
                getString(R.string.trakers),
                getString(R.string.track_your_progress_improve_and_go_towards_your_goal),
                R.drawable.ic_mood,
                object : ButtonClickListener {
                    override fun onClick() {
                        if (isUserExist) {
                            (activity as MainActivity).openTrackersActivity()
                        } else {
                            setupNeedAccountDialog()
                        }
                    }
                }),
            ButtonWithIconAndTextModel(
                3,
                getString(R.string.pomodoro_timer),
                getString(R.string.work_and_learn_using_the_pomodoro_technique),
                R.drawable.ic_pomodoro,
                object : ButtonClickListener {
                    override fun onClick() {
                        (activity as MainActivity).openPomodoroActivity()
                    }
                })
        )
        val adapter = ButtonWithIconAndTextAdapter()
        binding.recyclerView.adapter = adapter
        adapter.submitList(items)
    }

    private fun setupNeedAccountDialog() {
        val dialogFragment = NeedAccountDialogFragment()
        activity?.supportFragmentManager?.let { dialogFragment.show(it, "NEED_ACCOUNT_DIALOG") }
    }
}