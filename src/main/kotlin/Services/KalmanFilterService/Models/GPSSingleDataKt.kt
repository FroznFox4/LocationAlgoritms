package Services.KalmanFilterService.Models

data class GPSSingleDataKt (
    var speed: Float = 0f,
    val lon: Double = 0.0,
    val lat: Double = 0.0,
    val timestamp: Long = 0,
    val accuracy: Long = 0
)