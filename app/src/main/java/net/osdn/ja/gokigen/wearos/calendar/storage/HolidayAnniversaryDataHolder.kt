package net.osdn.ja.gokigen.wearos.calendar.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DataContent::class], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class HolidayAnniversaryDataHolder: RoomDatabase()
{
    abstract fun storageDao(): DataContentDao
}
