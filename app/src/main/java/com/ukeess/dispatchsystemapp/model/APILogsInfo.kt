package com.ukeess.dispatchsystemapp.model

import com.ukeess.dispatchsystemapp.enums.LogType

class APILogsInfo(val message: String, val jsonData: String, val typeMessage: LogType, val timeStamp: String)