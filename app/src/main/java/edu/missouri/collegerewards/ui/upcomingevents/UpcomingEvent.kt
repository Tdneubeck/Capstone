package edu.missouri.collegerewards.ui.upcomingevents

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class UpcomingEvent (
    val titleStringResourceID: String,
    val locationStringResourceID: String,
    val eventDateStringResourceID: String,
    val imageResourceID: String

)