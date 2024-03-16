package edu.missouri.collegerewards.ui.upcomingevents

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class UpcomingEvent (
    @StringRes val titleStringResourceID: Int,
    @StringRes val locationStringResourceID: Int,
    @DrawableRes val imageResourceID: Int
)