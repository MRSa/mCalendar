package net.osdn.ja.gokigen.wearos.calendar.presentation.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

enum class DateModification {
    NORMAL, HOLIDAY, ANNIVERSARY, NOTIFY, EVENT
}
class HolidayAnniversaryProvider : ViewModel()
{
    private var isRefreshing = false
    private val dateList = mutableStateListOf<String>()

    fun checkDate(calendar: Calendar) : DateModification
    {
        return (DateModification.NORMAL)
    }

    fun update(calendar: Calendar)
    {
        CoroutineScope(Dispatchers.Main).launch {
            if (!isRefreshing)
            {
                isRefreshing = true
                withContext(Dispatchers.Default) {
                    updateMonthlyData(calendar)
                }
                isRefreshing = false
            }
        }
    }

    private fun updateMonthlyData(calendar: Calendar)
    {
        val targetYear = calendar[Calendar.YEAR]
        val targetMonth = calendar[Calendar.MONTH] + 1
        Log.v(TAG, "  >>>>> $targetYear-$targetMonth ANNIVERSARY : ${dateList.size}")
    }

    companion object
    {
        private val TAG = HolidayAnniversaryProvider::class.java.simpleName
    }
}