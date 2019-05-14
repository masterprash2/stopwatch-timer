package life.circles.stopwatch.data

data class Timer(
    val remainingTime: Long,
    val lastStartTimeSinceBoot: Long,
    val lastClockTime: Long

)