package com.ukeess.dispatchsystemapp.model.api

class StartTripRequest(val timeStamp: String, val cmd: String, val paymentInfo: PaymentInfo, val tripId: String, val driverInfo: DriverInfo)