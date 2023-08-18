package net.osdn.ja.gokigen.wearos.calendar.presentation.ui

import java.util.Calendar


enum class DateModification {
    NORMAL, HOLIDAY, ANNIVERSARY, NOTIFY, NOTIFY2
}
class HolidayAnniversaryProvider
{
    fun checkDate(calendar: Calendar) : DateModification
    {
        return (DateModification.NORMAL)
    }

}