package com.sharp.githubuserprofile.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object AppUtilities {
    fun isValidString(input: String): Boolean {
        // Define a regular expression to match non-alphanumeric characters (including special characters)
        val regex = Regex("^[a-zA-Z0-9 ]*\$")
        return input.matches(regex)
    }

    fun getFormattedDate(inputDate: String): String {
        return if (inputDate.isNotEmpty()) {
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
            val instant = Instant.parse(inputDate)
            val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
            formatter.format(localDateTime)
        } else ""
    }

    val sortOptions = listOf("Forks", "Last Updated")
}