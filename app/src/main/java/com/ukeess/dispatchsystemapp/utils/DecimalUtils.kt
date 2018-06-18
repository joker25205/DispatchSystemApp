package com.ukeess.dispatchsystemapp.utils

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

private val BIG_DECIMAL_SCALE = 4

fun createBigDecimal(number: String): BigDecimal {
    return BigDecimal(number, MathContext.DECIMAL128).setScale(BIG_DECIMAL_SCALE, RoundingMode.HALF_UP)
}

fun createBigDecimal(number: Double): BigDecimal {
    return BigDecimal(number, MathContext.DECIMAL128).setScale(BIG_DECIMAL_SCALE, RoundingMode.HALF_UP)
}

fun createBigDecimal(number: Long): BigDecimal {
    return BigDecimal(number, MathContext.DECIMAL128).setScale(BIG_DECIMAL_SCALE, RoundingMode.HALF_UP)
}

fun toCents(usd: BigDecimal?): Int {
    return if (usd != null && usd.compareTo(BigDecimal.ZERO) > 0) {
        usd.movePointRight(2).toInt()
    } else 0
}

fun toCentsBigDecimals(usd: BigDecimal?): BigDecimal? {
    return if (usd != null && usd.compareTo(BigDecimal.ZERO) > 0) {
        usd.movePointRight(2)
    } else null
}

fun toDolars(usd: BigDecimal?): BigDecimal {
    if (usd != null && usd.compareTo(BigDecimal.ZERO) > 0) {
        val total = usd.toString()
        return BigDecimal(total).movePointLeft(2)
    }
    return BigDecimal(0.00)
}

fun round(value: BigDecimal, places: Int): BigDecimal {
    return value.setScale(places, RoundingMode.HALF_UP)
}


fun getString(value: BigDecimal, showZeroValues: Boolean): String {
    return if (showZeroValues || value.compareTo(BigDecimal.ZERO) > 0) {
        round(value, 2).toString()
    } else ""
}
