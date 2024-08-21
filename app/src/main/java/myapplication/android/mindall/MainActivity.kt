package myapplication.android.mindall

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import myapplication.android.mindall.databinding.ActivityMainBinding
import myapplication.android.mindall.presentation.plans.basicPlans.choosePlans.ChoosePlansActivity
import myapplication.android.mindall.presentation.plans.pomodoro.PomodoroTimeActivity
import myapplication.android.mindall.presentation.plans.trackers.TrackersActivity
import myapplication.android.mindall.presentation.profile.createAccount.main.CreateAccountActivity
import myapplication.android.mindall.presentation.profile.edit.ProfileEditActivity
import myapplication.android.mindall.presentation.profile.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_plans, R.id.navigation_profile
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun openProfileEditActivity(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(
            this,
            ProfileEditActivity::class.java
        )
       launcher.launch(intent)
    }

    fun openSettingsActivity(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(
            this,
            SettingsActivity::class.java
        )
        launcher.launch(intent)
    }

    fun openCreateAccountActivity(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(
            this,
            CreateAccountActivity::class.java
        )
        launcher.launch(intent)
    }

    fun openTrackersActivity(){
        val intent = Intent(
            this,
            TrackersActivity::class.java
        )
        startActivity(intent)
    }

    fun openPlansActivity(){
        val intent = Intent(
            this,
            ChoosePlansActivity::class.java
        )
        startActivity(intent)
    }

    fun openPomodoroActivity(){
        val intent = Intent(
            this,
            PomodoroTimeActivity::class.java
        )
        startActivity(intent)
    }
}