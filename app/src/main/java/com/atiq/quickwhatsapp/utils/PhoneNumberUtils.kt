package com.atiq.quickwhatsapp.utils

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

object PhoneNumberUtils {

    private val phoneUtil = PhoneNumberUtil.getInstance()

    fun buildWaUrl(dialCode: String, number: String): String {
        val cleaned = (dialCode + number).replace(Regex("[^0-9]"), "")
        return "https://wa.me/$cleaned"
    }

    fun isValid(dialCode: String, number: String): Boolean {
        return try {
            val fullNumber = "$dialCode$number"
            val parsed = phoneUtil.parse(fullNumber, null)
            phoneUtil.isValidNumber(parsed)
        } catch (e: NumberParseException) {
            false
        }
    }
}