package myapplication.android.mindall.presentation.plans.basicPlans.daily

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.DAILY_PLAN_ID
import myapplication.android.mindall.presentation.plans.basicPlans.daily.dailyPlanDetails.addSchedule.AddScheduleActivity

class DailyPlansActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily_plans)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun openAddScheduleActivity(id: String, launcher: ActivityResultLauncher<Intent>){
        val intent: Intent = Intent(this, AddScheduleActivity::class.java).apply {
            putExtra(DAILY_PLAN_ID, id)
        }
        launcher.launch(intent)
    }
}