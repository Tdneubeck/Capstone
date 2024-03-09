package edu.missouri.collegerewards.ui.upcomingevents

import edu.missouri.collegerewards.R

/*class to load upcoming events. Function that returns a list of UpcomingEvent objects to be displayed on screen
Strings are grabbed from the strings.xml file in the values folder - the strings will likely need to be added dynamically
from firebase to the strings.xml file
This will need to be updated to receive the events from firebase - Calvin*/
public final class UpcomingEventDataSource {
    fun loadUpcomingEvents(): List<UpcomingEvent>{
        return listOf<UpcomingEvent>(
            UpcomingEvent(R.string.testEvent1, R.drawable.mizzou),
            UpcomingEvent(R.string.testEvent2, R.drawable.mizzouvkansas),
            UpcomingEvent(R.string.testEvent3, R.drawable.mizzou),
            UpcomingEvent(R.string.testEvent4, R.drawable.mizzouvkansas),
            UpcomingEvent(R.string.testEvent5,R.drawable.mizzou),
        )
    }
}