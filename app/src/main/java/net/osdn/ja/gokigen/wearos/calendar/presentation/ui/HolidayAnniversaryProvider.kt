package net.osdn.ja.gokigen.wearos.calendar.presentation.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.osdn.ja.gokigen.wearos.calendar.DbSingleton
import net.osdn.ja.gokigen.wearos.calendar.storage.DataContent
import java.util.Calendar

enum class DateModification {
    NORMAL, HOLIDAY, ANNIVERSARY, NOTIFY, EVENT
}
class HolidayAnniversaryProvider : ViewModel()
{
    private val storageDao = DbSingleton.db.storageDao()
    private var isRefreshing = false
    private val dateList = mutableStateListOf<DataContent>()

    fun checkDate(calendar: Calendar) : DateModification
    {
        for (dateInfo in dateList)
        {
            val month = calendar[Calendar.MONTH] + 1
            val date = calendar[Calendar.DATE]
            if ((dateInfo.month == month)&&(dateInfo.date == date))
            {
                val info = when (dateInfo.attribute) {
                    0 -> DateModification.NORMAL
                    1 -> DateModification.HOLIDAY
                    2 -> DateModification.ANNIVERSARY
                    3 -> DateModification.NOTIFY
                    4 -> DateModification.EVENT
                    else -> DateModification.NORMAL
                }
                return (info)
            }
        }
        return (DateModification.NORMAL)
    }

    fun update(calendar: Calendar)
    {
        CoroutineScope(Dispatchers.Main).launch {
            if (!isRefreshing)
            {
                isRefreshing = true
                withContext(Dispatchers.Default) {
                    updateMonthlyData(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1)
                }
                isRefreshing = false
            }
        }
    }

    private fun updateMonthlyData(targetYear: Int, targetMonth: Int)
    {
        dateList.clear()
        dateList.addAll(storageDao.getContent(targetYear, targetMonth))
        Log.v(TAG, " UPDATED($targetYear-$targetMonth) : ${dateList.size}")
    }

    companion object
    {
        private val TAG = HolidayAnniversaryProvider::class.java.simpleName
    }
}
