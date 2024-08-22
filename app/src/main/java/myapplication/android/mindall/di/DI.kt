package myapplication.android.mindall.di

import myapplication.android.mindall.data.dto.userDto.FireBaseUserService
import myapplication.android.mindall.data.repository.DailyTaskRepository
import myapplication.android.mindall.data.repository.HabitTrackersRepository
import myapplication.android.mindall.data.repository.MonthlyPlanRepository
import myapplication.android.mindall.data.repository.MoodTrackerRepository
import myapplication.android.mindall.data.repository.NightTrackersRepository
import myapplication.android.mindall.data.repository.UserRepository
import myapplication.android.mindall.data.repository.WeeklyPlansRepository
import myapplication.android.mindall.data.repository.YearlyPlansRepository

class DI {
    companion object {
        val userRepository = UserRepository()
        val habitTrackersRepository = HabitTrackersRepository()
        val habitTrackers = HabitTrackersRepository.Trackers()
        val moodTrackerRepository = MoodTrackerRepository()
        val moodTracker = MoodTrackerRepository.Trackers()
        val monthlyPlanRepository = MonthlyPlanRepository()
        val monthlyTasks = MonthlyPlanRepository.MonthlyTasks()
        val monthlyGoals = MonthlyPlanRepository.MonthlyGoals()
        val dailyTaskRepository = DailyTaskRepository()
        val nightTrackersRepository = NightTrackersRepository()
        val nightTrackers = NightTrackersRepository.Trackers()
        val weeklyPlansRepository = WeeklyPlansRepository()
        val yearlyPlansRepository = YearlyPlansRepository()
        val yearlyGoals = YearlyPlansRepository.Goals()
        val yearlyDats = YearlyPlansRepository.Dates()
        val daysOfWeekPlans = WeeklyPlansRepository.DaysOfWeekPlans()
        val dailyTasks: DailyTaskRepository.Tasks = DailyTaskRepository.Tasks()
        val daySchedule: DailyTaskRepository.DaySchedule = DailyTaskRepository.DaySchedule()
        val service: FireBaseUserService? = FireBaseUserService.getInstance()
    }
}