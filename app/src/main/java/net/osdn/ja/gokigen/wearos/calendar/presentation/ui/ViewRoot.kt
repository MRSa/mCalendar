package net.osdn.ja.gokigen.wearos.calendar.presentation.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.MonthlyCalendarTheme
import java.util.Calendar

class ViewRoot @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AbstractComposeView(context, attrs, defStyleAttr)
{

    @Composable
    override fun Content()
    {
        val calendar = Calendar.getInstance()
        MonthlyCalendarTheme {
             MonthlyCalendar(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1, calendar[Calendar.DATE])
        }
        Log.v(TAG, " ... Monthly Calendar (${calendar[Calendar.YEAR]}-${calendar[Calendar.MONTH] + 1}) ...")
    }

    companion object
    {
        private val TAG = ViewRoot::class.java.simpleName
    }
}
