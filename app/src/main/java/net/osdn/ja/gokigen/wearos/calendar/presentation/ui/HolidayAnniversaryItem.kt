package net.osdn.ja.gokigen.wearos.calendar.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Text
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.Purple200
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.Red400
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.Teal200
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.White230
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.White230_2
import net.osdn.ja.gokigen.wearos.calendar.storage.DataContent

@Composable
fun HolidayAnniversaryItem(data: DataContent)
{
    val anniversarySize = 12.sp
    Row()
    {
        Text(
            text = "%02d-%02d ".format(data.month, data.date),
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
        3 -> White230_2   // Notify
        4 -> Purple200    // Event
        else -> White230  // Other
    }
    return (fontColor)
}
