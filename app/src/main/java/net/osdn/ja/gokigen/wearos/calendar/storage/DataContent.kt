package net.osdn.ja.gokigen.wearos.calendar.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity(tableName = "contents")
data class DataContent(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "attribute") val attribute: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "date") val date: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "event_date") val eventDate: Date?,
)
{
    companion object {
        fun create(attribute: Int, year: Int, month: Int, date:Int, title: String?, note: String?) : DataContent {
            val calendar = Calendar.getInstance()
            calendar.set(year, (month - 1), date)
            return DataContent(0, attribute, year, month, date, title, note, calendar.time)
        }
    }
}
