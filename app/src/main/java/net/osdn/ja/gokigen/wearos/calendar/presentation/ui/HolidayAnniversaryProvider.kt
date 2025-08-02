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
    val dateList = mutableStateListOf<DataContent>()

    init {
        update(Calendar.getInstance())
    }

    fun checkDate(calendar: Calendar) : DateModification
    {
        var isHoliday = false
        var isAnniversary = false
        var isNotify = false
        var isEvent = false
        for (dateInfo in dateList)
        {
            val month = calendar[Calendar.MONTH] + 1
            val date = calendar[Calendar.DATE]
            if ((dateInfo.month == month)&&(dateInfo.date == date))
            {
                when (dateInfo.attribute) {
                    0 -> { }
                    1 -> { isHoliday = true }
                    2 -> { isAnniversary = true }
                    3 -> { isNotify = true }
                    4 -> { isEvent = true }
                    else -> { }
                }
            }
        }
        // ----- Notify > Event > Anniversary > Holiday > Normal の順に判定
        return (if (isNotify)
        {
            DateModification.NOTIFY
        }
        else if (isEvent)
        {
            DateModification.EVENT
        }
        else if (isAnniversary)
        {
            DateModification.ANNIVERSARY
        }
        else if (isHoliday)
        {
            DateModification.HOLIDAY
        }
        else
        {
            DateModification.NORMAL
        })
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
