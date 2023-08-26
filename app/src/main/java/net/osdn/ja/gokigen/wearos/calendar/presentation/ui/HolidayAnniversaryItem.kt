package net.osdn.ja.gokigen.wearos.calendar.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.Amber500
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.Purple200
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.Red400
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.Teal200
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.White230
import net.osdn.ja.gokigen.wearos.calendar.storage.DataContent

@Composable
fun HolidayAnniversaryItem(data: DataContent)
{
    val anniversarySize = 12.sp
    Row(
        Modifier.padding(start = 32.dp)
    )
    {
        Text(
            //text = "%02d-%02d ".format(data.month, data.date),
            text = "%02d/%02d ".format(data.month, data.date),
            //text = " %02d ".format(data.date),
            color = decideFontColor(data),
            fontWeight = FontWeight.Bold,
            fontSize = anniversarySize,
            textAlign = TextAlign.Center,
        )

        Text(
            text = data.title?: "",
            color = decideFontColor(data),
            fontWeight = FontWeight.Bold,
            fontSize = anniversarySize,
            textAlign = TextAlign.Center,
        )
    }
}

private fun decideFontColor(data: DataContent) : Color
{
    val fontColor = when (data.attribute) {
        0 -> White230     // Normal
        1 -> Red400       // Holiday
        2 -> Teal200      // Anniversary
        3 -> Amber500     // Notify
        4 -> Purple200    // Event
        else -> White230  // Other
    }
    return (fontColor)
}