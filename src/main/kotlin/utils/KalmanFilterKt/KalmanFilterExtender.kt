package utils.KalmanFilterKt

interface KalmanFilterExtender<T>: KalmanFilter {
    fun setState(point: T)
    fun process(data: T)
    fun getCollection(): ArrayList<T>
}