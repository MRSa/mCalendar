package net.osdn.ja.gokigen.wearos.calendar.presentation.ui

import android.text.format.DateFormat
import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.scrollAway
import kotlinx.coroutines.launch
import net.osdn.ja.gokigen.wearos.calendar.R
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.MonthlyCalendarTheme
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.wearColorPalette
import java.util.Calendar
import java.util.Locale

@Composable
fun MonthlyCalendar(initialYear: Int, initialMonth: Int, initialDate: Int)
{
    var year by remember { mutableStateOf(initialYear) }
    var month by remember { mutableStateOf(initialMonth) }
    var date by remember { mutableStateOf(initialDate) }

    MonthlyCalendarTheme {
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()

        var showYear = year
        var showMonth = month
        val showTitleYearMonth =
            if (checkIsShowNextMonth(year, month, date))
            {
                // 翌月の表示にする
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(year, month, 1)
                showYear = calendar[Calendar.YEAR]
                showMonth = calendar[Calendar.MONTH] + 1
                "%04d-%02d".format(showYear, showMonth)
            }
            else
            {
                // 当月を表示する
                "%04d-%02d".format(showYear, showMonth)
            }

        Scaffold(
            timeText = {
                TimeText(
                    timeSource = TimeTextDefaults.timeSource(
                        DateFormat.getBestDateTimePattern(
                            Locale.getDefault(),
                            "HH:mm"
                        )
                    ),
                    modifier = Modifier.scrollAway(scrollState = scrollState)
                )
            },
            positionIndicator = {
                PositionIndicator(scrollState = scrollState)
            },
        ) {
            Column(
                modifier = Modifier
                    .onRotaryScrollEvent {
                        coroutineScope.launch {
                            scrollState.scrollBy(it.verticalScrollPixels)
                        }
                        true
                    }
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .focusRequester(focusRequester)
                    .focusable(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Row(Modifier.align(Alignment.CenterHorizontally))
                {
                    Text(
                        text = showTitleYearMonth,
                        color = wearColorPalette.primary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                    )
                }
                Column() {
                    Text(
                        text = " SU MO TU WE TH FR SA ",
                        //color = defaultColorPalette.primary,
                        fontWeight = FontWeight.Normal,
                        color = wearColorPalette.secondary,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                    )

                    val calendar: Calendar = Calendar.getInstance()
                    calendar.set(showYear, (showMonth - 1), 1)
                    calendar.add(Calendar.DATE, getDayOfWeekIndex(calendar) * (-1))

                    for (index in 1..6)
                    {
                        var dayString = ""
                        for (dayOfWeek in 1 .. 7)
                        {
                            if ((date == calendar[Calendar.DATE])&&(month - 1 == calendar[Calendar.MONTH]))
                            {
                                dayString += "[%02d]".format(calendar[Calendar.DATE])
                            }
                            else
                            {
                                dayString += " %02d ".format(calendar[Calendar.DATE])
                            }
                            calendar.add(Calendar.DATE, 1)
                        }
                        Text(
                            text = dayString,
                            //color = defaultColorPalette.primary,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp,
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Row(Modifier.align(Alignment.CenterHorizontally))
                {
                    Button(
                        onClick = {
                            try
                            {
                                Log.d("Button(Previous)", "onClick")
                                val calendar = Calendar.getInstance()
                                calendar.set(year, month - 2, date)
                                month = calendar[Calendar.MONTH] + 1
                            }
                            catch (e: Exception)
                            {
                                e.printStackTrace()
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(48.dp),
                        enabled = true
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_left_24),
                            contentDescription = "Previous",
                            tint = Color.LightGray
                        )
                    }
                    Button(
                        onClick = {
                            try
                            {
                                Log.d("Button(Today)", "onClick")
                                val calendar = Calendar.getInstance()
                                year = calendar[Calendar.YEAR]
                                month = calendar[Calendar.MONTH] + 1
                                date = calendar[Calendar.DATE]
                            }
                            catch (e: Exception)
                            {
                                e.printStackTrace()
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(48.dp),
                        enabled = true
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_today_24),
                            contentDescription = "Today",
                            tint = Color.LightGray
                        )
                    }
                    Button(
                        onClick = {
                            try
                            {
                                Log.d("Button(Next)", "onClick")
                                val calendar = Calendar.getInstance()
                                calendar.set(year, month, date)
                                month = calendar[Calendar.MONTH] + 1
                            }
                            catch (e: Exception)
                            {
                                e.printStackTrace()
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(48.dp),
                        enabled = true
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_right_24),
                            contentDescription = "Next",
                            tint = Color.LightGray
                        )
                    }
                }
            }
        }
    }
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

private fun checkIsShowNextMonth(year: Int, month: Int, checkDay: Int) : Boolean
{
    // ”指定された日" から、翌月の開始日を確認
    val calendar: Calendar = Calendar.getInstance()
    calendar.set(year, month, 1)
    calendar.add(Calendar.DATE, getDayOfWeekIndex(calendar) * (-1))
    return (checkDay >= calendar[Calendar.DATE])
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val calendar = Calendar.getInstance()
    MonthlyCalendar(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1, 1)
}
