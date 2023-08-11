package net.osdn.ja.gokigen.wearos.calendar.tile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.material.ChipColors
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import com.google.android.horologist.compose.tools.LayoutRootPreview
import java.util.Calendar

class MyTimelineLayout
{
    private var startDayOfWeek = 0
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay = 0

    init
    {
        try
        {
            // カレンダーから今日の日付を認識する
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            currentYear = calendar[Calendar.YEAR]        // Year
            currentMonth = calendar[Calendar.MONTH] + 1  // Month: 0 - 11
            currentDay = calendar[Calendar.DATE]         // Day

            // 当月1日の曜日を求める
            calendar.set(currentYear, (currentMonth - 1), 1)
            startDayOfWeek = getDayOfWeekIndex(calendar)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        Log.v(TAG, " ----- TODAY : $currentYear-$currentMonth-$currentDay ($startDayOfWeek)")
    }

    fun getLayout(context: Context): LayoutElementBuilders.LayoutElement
    {
        return (tileLayout(context))
    }

    private fun getDayOfWeekIndex(calendar: Calendar): Int {
        var week = 0
        when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> week = 1
            Calendar.TUESDAY -> week = 2
            Calendar.WEDNESDAY -> week = 3
            Calendar.THURSDAY -> week = 4
            Calendar.FRIDAY -> week = 5
            Calendar.SATURDAY -> week = 6
            Calendar.SUNDAY -> {}
            else -> {}
        }
        return (week)
    }

    companion object
    {
        private val TAG = MyTimelineLayout::class.java.simpleName
    }
}
private fun tileLayout(context: Context): LayoutElementBuilders.LayoutElement
{
    val theme = Colors(
        /*primary=*/ 0xFFD0BCFF.toInt(), /*onPrimary=*/ 0xFF381E72.toInt(),
        /*surface=*/ 0xFF202124.toInt(), /*onSurface=*/ 0xFFFFFFFF.toInt()
    )
    val deviceParameters = DeviceParametersBuilders.DeviceParameters.Builder().build()
    val clickable = ModifiersBuilders.Clickable.Builder().build()
    val chipColors = ChipColors.primaryChipColors(theme)
    val timeline =
        PrimaryLayout.Builder(deviceParameters)
            .setPrimaryChipContent(
                CompactChip.Builder(context, "More",  clickable, deviceParameters)
                    .setChipColors(chipColors)
                    .build()
            )
            .build()
    return (timeline)
}

@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)

@Composable
fun TilePreview() {
    LayoutRootPreview(root = tileLayout(LocalContext.current))
}
