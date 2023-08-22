package net.osdn.ja.gokigen.wearos.calendar.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import net.osdn.ja.gokigen.wearos.calendar.presentation.theme.wearColorPalette
import net.osdn.ja.gokigen.wearos.calendar.storage.DataContent

@Composable
fun HolidayAnniversaryItem(data: DataContent)
{
    val anniversarySize = 12.sp
    Row()
    {
        Text(
            text = "%02d-%02d ".format(data.month, data.date),
            color = wearColorPalette.primary,
            fontWeight = FontWeight.Bold,
            fontSize = anniversarySize,
            textAlign = TextAlign.Center,
        )

        Text(
            text = data.title?: "",
            color = wearColorPalette.primary,
            fontWeight = FontWeight.Bold,
            fontSize = anniversarySize,
            textAlign = TextAlign.Center,
        )

    }
}
