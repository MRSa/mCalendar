package net.osdn.ja.gokigen.wearos.calendar.tile

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.material.ChipColors
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import com.google.android.horologist.compose.tools.LayoutRootPreview
import java.util.Calendar

class MyTimelineLayout
{
    fun getLayout(context: Context, requestParams: RequestBuilders.TileRequest): LayoutElementBuilders.LayoutElement
    {
        return (tileLayout(context, requestParams))
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

    private fun checkIsShowNextMonth(year: Int, month: Int, checkDay: Int) : Boolean
    {
        // ”指定された日" から、翌月の開始日を確認
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        calendar.add(Calendar.DATE, getDayOfWeekIndex(calendar) * (-1))
        return (checkDay >= calendar[Calendar.DATE])
    }

    private fun getMonthlyCalendarText(year: Int, month: Int, targetMonth: Int, targetDate: Int): String
    {
        // 月間カレンダーの文字列を生成する
        val label = " SU MO TU WE TH FR SA \n"
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, (month - 1), 1)
        val startDayOfWeek = getDayOfWeekIndex(calendar)
        calendar.add(Calendar.DATE, startDayOfWeek * (-1))

        var dayString = ""
        for (index in 1..6)
        {
            for (dayOfWeek in 1 .. 7)
            {
                if ((targetDate == calendar[Calendar.DATE])&&(targetMonth - 1 == calendar[Calendar.MONTH]))
                {
                    dayString += "[%02d]".format(calendar[Calendar.DATE])
                }
                else
                {
                    dayString += " %02d ".format(calendar[Calendar.DATE])
                }
                calendar.add(Calendar.DATE, 1)
            }
            dayString += "\n"
        }
        return ("$label$dayString")
    }

    private fun tileLayout(context: Context,  requestParams: RequestBuilders.TileRequest): LayoutElementBuilders.LayoutElement
    {
        // 今日を取得する
        val today: Calendar = Calendar.getInstance()
        val targetYear = today[Calendar.YEAR]
        val targetMonth = today[Calendar.MONTH] + 1
        val targetDate = today[Calendar.DATE]

        // 表示する年と月
        var year = targetYear
        var month  = targetMonth

        // 月末あたりになっていて、表示がすくなくなりそうな場合
        val showTitleYearMonth =
            if (checkIsShowNextMonth(targetYear, targetMonth, targetDate))
            {
                // 翌月の表示にする
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(year, targetMonth, 1)
                year = calendar[Calendar.YEAR]
                month = calendar[Calendar.MONTH] + 1
                "%04d-%02d".format(year, month)
            }
            else
            {
                // 当月を表示する
                "%04d-%02d".format(year, month)
            }
        val deviceParameters = requestParams.deviceConfiguration
        val clickable = ModifiersBuilders.Clickable.Builder().build()
        val timeline =
            PrimaryLayout.Builder(deviceParameters)
                .setPrimaryLabelTextContent(
                    Text.Builder(context, showTitleYearMonth)
                        .setModifiers(
                            ModifiersBuilders.Modifiers.Builder()
                                .setClickable(clickable)
                                .build())
                        .setColor(ColorBuilders.ColorProp.Builder(0xFFD0BCFF.toInt()).build())
                        .setTypography(Typography.TYPOGRAPHY_TITLE3)
                        .build()
                )
                .setSecondaryLabelTextContent(
                    Text.Builder(context, getMonthlyCalendarText(year, month, targetMonth, targetDate))
                        .setMaxLines(7)
                        .setModifiers(
                            ModifiersBuilders.Modifiers.Builder()
                                .setClickable(clickable)
                                .build())
                        .setColor(ColorBuilders.ColorProp.Builder(0xFFE5E5E5.toInt()).build())
                        .setTypography(Typography.TYPOGRAPHY_CAPTION2)
                        .build()
                )
                .build()
        return (timeline)
    }
}
private fun tileLayout0(context: Context): LayoutElementBuilders.LayoutElement
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
                CompactChip.Builder(context, "Calendar",  clickable, deviceParameters)
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
    LayoutRootPreview(root = tileLayout0(LocalContext.current))
}
