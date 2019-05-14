package life.circles.stopwatch.formDialog.interactor

class MinutesValidator {

    companion object {
        fun validate(duration: String): Triple<Boolean, String?, Int> {
            var valid = true
            var message: String? = null
            var time: Int = 0
            if (duration.isBlank()) {
                valid = false
                message = "Duration not set"
            } else {
                try {
                    val durationInt = duration.toInt()
                    if (durationInt > 60) {
                        valid = false
                        message = "Duration should be less than 60"
                        time = 60;
                    } else if (durationInt <= 0) {
                        valid = false
                        time = 0;
                        message = "Duration should be between 1 to 60"
                    } else {
                        valid = true;
                        time = durationInt;
                    }
                } catch (e: Exception) {
                    valid = false
                    message = e.message
                    time = 0;
                }
            }

            return Triple(valid, message, time)
        }

    }

}