package myapplication.android.mindall.domain.useCase.monthly.tasks

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.data.dto.commonDtos.TaskDto
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.common.TaskMainModel
import java.util.stream.Collectors

class GetMonthlyTaskUseCase {
    fun invoke(
        yearId: String,
        monthId: String
    ): Single<List<TaskMainModel>> {
        return DI.monthlyTasks.getMonthlyTasks(yearId, monthId).map { dtos ->
            dtos.stream()
                .map { dto -> TaskMainModel(dto.id, dto.title, dto.isCompleted, dto.flag) }
                .collect(Collectors.toList())
        }
    }
}