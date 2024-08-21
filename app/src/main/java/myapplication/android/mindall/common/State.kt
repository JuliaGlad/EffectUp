package myapplication.android.mindall.common

data class State<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): State<T> = State(Status.SUCCESS, data, null)

        fun <T> error(msg: String): State<T> = State(Status.ERROR, null, msg)

        fun <T> loading(): State<T> = State(Status.LOADING,null, null)
    }
}