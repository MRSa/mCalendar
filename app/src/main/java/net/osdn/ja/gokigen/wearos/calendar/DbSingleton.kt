package net.osdn.ja.gokigen.wearos.calendar

import android.app.Application
import androidx.room.Room
import net.osdn.ja.gokigen.wearos.calendar.storage.HolidayAnniversaryDataHolder

class DbSingleton : Application()
{
    companion object
    {
        lateinit var db: HolidayAnniversaryDataHolder
    }

    override fun onCreate()
    {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            HolidayAnniversaryDataHolder::class.java, "date-data-database"
        ).build()
    }
}
