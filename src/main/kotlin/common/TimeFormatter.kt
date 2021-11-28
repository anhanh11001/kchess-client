package common

object TimeFormatter {

    fun formatTimeInMillisToReadableString(timeInSeconds: Long): String {
        val hour = (timeInSeconds / 3600).toInt()
        val minute = ((timeInSeconds % 3600) / 60).toInt()
        val second = ((timeInSeconds % 3600) % 60).toInt()

        fun convertNumber(num: Int): String = if (num < 10) {
            "0$num"
        } else {
            "$num"
        }

        return "${convertNumber(hour)}:${convertNumber(minute)}:${convertNumber(second)}"
    }
}