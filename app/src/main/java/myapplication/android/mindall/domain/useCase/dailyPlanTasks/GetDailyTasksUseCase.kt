package myapplication.android.mindall.domain.useCase.dailyPlanTasks

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.common.TaskMainModel
import java.util.stream.Collectors

class GetDailyTasksUseCase {
    fun invoke(dailyPlanId: String): Single<MutableList<TaskMainModel>>{
        return DI.dailyTasks.getTasks(dailyPlanId).map{dtos ->
            dtos.stream()
                .map{dto -> TaskMainModel(
                    dto.id,
                    dto.title,
                    dto.isCompleted,
                    dto.flag)
                }.collect(Collectors.toList())
        }
    }
}