package myapplication.android.mindall.presentation.plans.pomodoro.pomodoroTimer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PomodoroTimerViewModel : ViewModel() {

    private val _turn = MutableLiveData<Int?>(null)
    var turn = _turn

    fun increaseValue() {
        if (turn.value != null) {
            _turn.value = _turn.value?.plus(1)
        } else {
            _turn.value = 1
        }
    }

    fun getTurn(): Int? {
        return _turn.value
    }

    fun setValue(int: Int) {
        _turn.value = int
    }

}