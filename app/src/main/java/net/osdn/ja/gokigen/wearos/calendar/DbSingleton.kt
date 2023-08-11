package net.osdn.ja.gokigen.wearos.calendar

import android.app.Application

class DbSingleton : Application()
{
    companion object
    {
        //lateinit var db: DataHolder
    }

    override fun onCreate()
    {
        super.onCreate()
/*
        db = Room.databaseBuilder(
            applicationContext,
            DataHolder::class.java, "data-database"
        ).build()
*/
    }
}
