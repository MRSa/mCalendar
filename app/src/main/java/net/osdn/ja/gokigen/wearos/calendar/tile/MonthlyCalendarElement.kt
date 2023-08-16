package net.osdn.ja.gokigen.wearos.calendar.tile

import android.content.Context
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import net.osdn.ja.gokigen.wearos.calendar.R
import java.util.Calendar

class MonthlyCalendarElement(private val context: Context)
{
    // 今日を取得して記憶する
    private val today: Calendar = Calendar.getInstance()
    private val targetYear = today[Calendar.YEAR]
    private val targetMonth = today[Calendar.MONTH] + 1
    private val targetDate = today[Calendar.DATE]

    // 表示する年と月
    private var year = targetYear
    private var month  = targetMonth

    init {
        // ”指定された日" から、翌月の開始日を確認し、当日表示が最下行にくる場合は、翌月表示にする
        if (checkIsShowNextMonth(targetYear, targetMonth, targetDate))
        {
            // 翌月の表示にする
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, targetMonth, 1)
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH] + 1
        }
    }

    private fun checkIsShowNextMonth(checkYear: Int, checkMonth: Int, checkDay: Int) : Boolean
    {
        // 指定された日が翌月１日と同じ週かどうか確認する
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(checkYear, checkMonth, 1)
        calendar.add(Calendar.DATE, getDayOfWeekIndex(calendar) * (-1))
        return (checkDay >= calendar[Calendar.DATE])
    }

    private fun getDayOfWeekIndex(calendar: Calendar): Int
    {
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
    private fun getDayOfWeekColor(calendar: Calendar): Int
    {
        return (when (calendar[Calendar.DAY_OF_WEEK]) {
                    Calendar.MONDAY -> 0xFFE6E6E6.toInt()
                    Calendar.TUESDAY -> 0xFFE6E6E6.toInt()
                    Calendar.WEDNESDAY ->  0xFFE6C6E6.toInt()
                    Calendar.THURSDAY -> 0xFFE6E6E6.toInt()
                    Calendar.FRIDAY -> 0xFFE6E6E6.toInt()
                    Calendar.SATURDAY -> 0xFF85BADE.toInt()
                    Calendar.SUNDAY -> 0xFFCF6679.toInt()
                    else -> 0xFFE6E6E6.toInt()
                })
    }

    private fun drawDayOfWeekTitle(clickable: ModifiersBuilders.Clickable): LayoutElementBuilders.LayoutElement
    {
        val backColor = 0xFF000000.toInt()
        val row = LayoutElementBuilders.Row.Builder()
        row.setModifiers(ModifiersBuilders.Modifiers.Builder()
            .setClickable(clickable)
            .setBackground(
                ModifiersBuilders.Background.Builder().
                setColor(ColorBuilders.ColorProp.Builder(backColor).build())
                    .build())
            .build())
        row.addContent(getTextContent(clickable, context.getString(R.string.label_sunday) + " ", 0xFFCF6679.toInt(), backColor))
        row.addContent(getTextContent(clickable, context.getString(R.string.label_monday) + " ", 0xFFE6E6E6.toInt(), backColor))
        row.addContent(getTextContent(clickable, context.getString(R.string.label_tuesday) + " ", 0xFFE6E6E6.toInt(), backColor))
        row.addContent(getTextContent(clickable, context.getString(R.string.label_wednesday) + " ", 0xFFE6C6E6.toInt(), backColor))
        row.addContent(getTextContent(clickable, context.getString(R.string.label_thursday) + " ", 0xFFE6E6E6.toInt(), backColor))
        row.addContent(getTextContent(clickable, context.getString(R.string.label_friday) + " ", 0xFFE6E6E6.toInt(), backColor))
        row.addContent(getTextContent(clickable, context.getString(R.string.label_saturday) + " ", 0xFF85BADE.toInt(), backColor))
        return (row.build())
    }

    private fun getTextContent(clickable: ModifiersBuilders.Clickable, label: String, foreColor: Int, backColor: Int): LayoutElementBuilders.LayoutElement
    {
        return (Text.Builder(context, label)
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(clickable)
                    .setBackground(
                        ModifiersBuilders.Background.Builder().
                        setColor(ColorBuilders.ColorProp.Builder(backColor).build())
                            .build())
                    .build())
            .setColor(ColorBuilders.ColorProp.Builder(foreColor).build())
            .setTypography(Typography.TYPOGRAPHY_CAPTION2)
            .build())
    }

    fun getMonthlyCalendarLayout(clickable: ModifiersBuilders.Clickable): LayoutElementBuilders.LayoutElement
    {
        val column = LayoutElementBuilders.Column.Builder()

        // タイトルを入れる
        column.addContent(drawDayOfWeekTitle(clickable))

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, (month - 1), 1)
        calendar.add(Calendar.DATE, getDayOfWeekIndex(calendar) * (-1))

        // 5週分のカレンダーを表示
        for (index in 1..5)
        {
            val backColor = if ((index % 2) == 0) { 0xFF000000.toInt() } else { 0xFF505050.toInt() }
            val row = LayoutElementBuilders.Row.Builder()
            row.setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(clickable)
                    .setBackground(
                        ModifiersBuilders.Background.Builder().setColor(
                            ColorBuilders.ColorProp.Builder(backColor).build()
                        )
                        .build())
                    .build()
            )
            for (dayOfWeek in 1..7)
            {
                var dayString = ""
                var foregroundColor = getDayOfWeekColor(calendar)
                var backgroundColor = backColor
                if ((targetDate == calendar[Calendar.DATE]) && (targetMonth - 1 == calendar[Calendar.MONTH]) && (targetYear == calendar[Calendar.YEAR])) {
                    foregroundColor = backColor
                    backgroundColor = getDayOfWeekColor(calendar)
                }
                dayString += " %02d ".format(calendar[Calendar.DATE])
                row.addContent(getTextContent(clickable, dayString, foregroundColor, backgroundColor))
                calendar.add(Calendar.DATE, 1)
            }
            column.addContent(row.build())
        }
        return (column.build())
    }

    fun getMonthlyCalendarTitleLayout(clickable: ModifiersBuilders.Clickable): LayoutElementBuilders.LayoutElement
    {
        // カレンダータイトルを表示する
        return (Text.Builder(context, "%04d-%02d".format(year, month))
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(clickable)
                    .build())
            .setColor(ColorBuilders.ColorProp.Builder(0xFFD0BCFF.toInt()).build())
            .setTypography(Typography.TYPOGRAPHY_TITLE3)
            .build()
                )
    }
}
