package com.dicoding.picodiploma.productdetail

import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//number format
fun String.withNumberingFormat(): String {
    return NumberFormat.getNumberInstance().format(this.toDouble())
}

fun String.withDateFormat(): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}

fun String.withCurrencyFormat(): String {
    val rupiahExchangeRate = 14000.00
    val euroExchangeRate = 0.88

    var priceOnDoller = this.toDouble() / rupiahExchangeRate

    var mCurrencyFormat = NumberFormat.getCurrencyInstance()
    val deviceLocale = Locale.getDefault().country

    when {
        deviceLocale.equals("ES") -> {
            priceOnDoller *= euroExchangeRate
        }
        deviceLocale.equals("ID") -> {
            priceOnDoller *= rupiahExchangeRate
        }

        else -> {
            mCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        }
    }
    return mCurrencyFormat.format(priceOnDoller)
}

