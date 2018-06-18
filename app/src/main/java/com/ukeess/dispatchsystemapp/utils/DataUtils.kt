package com.ukeess.dispatchsystemapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentTime(): String {
    return SimpleDateFormat("HH:mm:ss.SSS").format(Date())
}