package com.ukeess.dispatchsystemapp.model.api

class RequestPayment(val timeStamp: String, val cmd: String, val paymentInfo: PaymentInfo, val isInvoiceTrip: Boolean, val isFlatRateTrip: Boolean)