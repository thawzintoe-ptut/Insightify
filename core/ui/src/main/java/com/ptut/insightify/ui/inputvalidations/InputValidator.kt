package com.ptut.insightify.ui.inputvalidations

import com.ptut.insightify.ui.R

object InputValidator {
    private const val MIN_PASSWORD_LENGTH = 8

    fun getEmailErrorIdOrNull(input: String): Int? {
        return if (!isValidEmail(input)) {
            R.string.invalid_email
        } else {
            null
        }
    }

    fun getPasswordErrorIdOrNull(input: String): Int? {
        return if (input.length < MIN_PASSWORD_LENGTH) {
            R.string.password_too_short
        } else {
            null
        }
    }

    // Email validation based on regex pattern
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}\$"
        return email.matches(emailPattern.toRegex())
    }
}
