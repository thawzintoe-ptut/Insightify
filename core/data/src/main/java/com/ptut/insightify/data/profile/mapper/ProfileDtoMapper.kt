package com.ptut.insightify.data.profile.mapper

import com.ptut.insightify.data.profile.model.ProfileAttributes
import com.ptut.insightify.domain.profile.model.UserProfile
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun ProfileAttributes.mapToDomain() = UserProfile(
    profileImageUrl = avatarUrl,
    email = email,
    name = name,
    currentDate = formattedDateTime,
)

fun getCurrentDateTimeFormatted(): String {
    val currentMoment = Clock.System.now()
    val currentDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
    val dayOfWeek = currentDateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    val month = currentDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val year = currentDateTime.year
    return "$dayOfWeek, $month $year"
}

// Use the function to get the formatted date time string
val formattedDateTime = getCurrentDateTimeFormatted()