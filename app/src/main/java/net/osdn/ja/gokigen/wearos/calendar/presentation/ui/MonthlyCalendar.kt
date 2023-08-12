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
import androidx.compose.runtime.mutableIntStateOf
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
    var year by remember { mutableIntStateOf(initialYear) }
    var month by remember { mutableIntStateOf(initialMonth) }
    var date by remember { mutableIntStateOf(initialDate) }

    MonthlyCalendarTheme {
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        val yearMonthSize = 16.sp
        val dateSize = 12.sp

        val showTitleYearMonth =  "%04d-%02d".format(year, month)

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
                    .padding(horizontal = 20.dp, vertical = 20.dp)
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
                        fontSize = yearMonthSize,
                    )
                }
                Column {
                    Text(
                        text = " SU MO TU WE TH FR SA ",
                        //color = defaultColorPalette.primary,
                        fontWeight = FontWeight.Normal,
                        color = wearColorPalette.secondary,
                        textAlign = TextAlign.Center,
                        fontSize = dateSize,
                    )

                    val calendar: Calendar = Calendar.getInstance()
                    val currentYear = calendar[Calendar.YEAR]
                    val currentMonth = calendar[Calendar.MONTH] + 1
                    val currentDate = calendar[Calendar.DATE]
                    calendar.set(year, (month - 1), 1)
                    calendar.add(Calendar.DATE, getDayOfWeekIndex(calendar) * (-1))

                    for (index in 1..6)
                    {
                        var dayString = ""
                        for (dayOfWeek in 1 .. 7)
                        {
                            dayString += if ((currentDate == calendar[Calendar.DATE])&&(currentMonth - 1 == calendar[Calendar.MONTH])&&(currentYear == calendar[Calendar.YEAR])) {
                                "[%02d]".format(calendar[Calendar.DATE])
                            } else {
                                " %02d ".format(calendar[Calendar.DATE])
                            }
                            calendar.add(Calendar.DATE, 1)
                        }
                        Text(
                            text = dayString,
                            //color = defaultColorPalette.primary,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            fontSize = dateSize,
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
                                val calendar = Calendar.getInstance()
                                calendar.set(year, month - 1, date)
                                calendar.add(Calendar.MONTH, -1)
                                month = calendar[Calendar.MONTH] + 1
                                year = calendar[Calendar.YEAR]
                                Log.d("Button(Previous)", "onClick $year-$month-$date")
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
                                val calendar = Calendar.getInstance()
                                year = calendar[Calendar.YEAR]
                                month = calendar[Calendar.MONTH] + 1
                                date = calendar[Calendar.DATE]
                                Log.d("Button(Today)", "onClick  $year-$month-$date")
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
                                val calendar = Calendar.getInstance()
                                calendar.set(year, month - 1, date)
                                calendar.add(Calendar.MONTH, 1)
                                month = calendar[Calendar.MONTH] + 1
                                year = calendar[Calendar.YEAR]
                                Log.d("Button(Next)", "onClick  $year-$month-$date")
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

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val calendar = Calendar.getInstance()
    MonthlyCalendar(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1, 1)
}
