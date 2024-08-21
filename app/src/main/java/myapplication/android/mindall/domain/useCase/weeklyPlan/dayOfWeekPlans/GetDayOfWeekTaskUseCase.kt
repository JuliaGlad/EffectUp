package myapplication.android.mindall.domain.useCase.weeklyPlan.dayOfWeekPlans

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.common.TaskMainModel
import java.util.stream.Collectors

class GetDayOfWeekTaskUseCase {
    fun invoke(weekPlansId: String?, dayOfWeekId: String?) : Single<List<TaskMainModel>>{
        return DI.daysOfWeekPlans.getDayOfWeekTasks(weekPlansId, dayOfWeekId).map { dtos ->
            dtos.stream()
                .map { dto -> TaskMainModel(dto.id, dto.title, dto.isCompleted, dto.flag)}
                .collect(Collectors.toList())
        }
    }
}