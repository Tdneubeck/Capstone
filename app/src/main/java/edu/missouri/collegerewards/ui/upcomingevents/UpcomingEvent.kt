package edu.missouri.collegerewards.ui.upcomingevents

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class UpcomingEvent (
    @StringRes val stringResourceID: Int,
    @DrawableRes val imageResourceID: Int
)