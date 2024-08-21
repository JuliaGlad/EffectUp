package myapplication.android.mindall.presentation.plans.basicPlans.choosePlans

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import myapplication.android.mindall.R
import myapplication.android.mindall.presentation.plans.basicPlans.daily.DailyPlansActivity
import myapplication.android.mindall.presentation.plans.basicPlans.monthly.MonthPlansActivity
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.WeeklyCalendarActivity
import myapplication.android.mindall.presentation.plans.basicPlans.yearly.YearlyPlansActivity

class ChoosePlansActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose_plans)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun openDailyPlansActivity(){
        val intent = Intent(
            this,
            DailyPlansActivity::class.java
        )
        startActivity(intent)
    }

    fun openWeeklyPlansActivity(){
        val intent = Intent(
            this,
            WeeklyCalendarActivity::class.java
        )
        startActivity(intent)
    }

    fun openMonthlyPlansActivity(){
        val intent = Intent(
            this,
            MonthPlansActivity::class.java
        )
        startActivity(intent)
    }

    fun openYearlyPlansActivity(){
        val intent = Intent(
            this,
            YearlyPlansActivity::class.java
        )
        startActivity(intent)
    }
}