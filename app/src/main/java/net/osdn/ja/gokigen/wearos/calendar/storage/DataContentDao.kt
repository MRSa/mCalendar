package net.osdn.ja.gokigen.wearos.calendar.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataContentDao
{
    @Query("SELECT COUNT(*) FROM contents")
    fun getCount(): Int
    @Query("SELECT * FROM contents")
    fun getAll(): List<DataContent>

    @Query("SELECT * FROM contents WHERE (year = :yearData OR year < 0) AND month = :monthData")
    fun getContent(yearData: Int, monthData: Int): List<DataContent>

    @Query("DELETE FROM contents")
    fun deleteAll()

    @Query("DELETE FROM contents WHERE year < :yearData AND year >= 0")
    fun deleteBeforeByYear(yearData: Int)
    @Query("DELETE FROM contents WHERE year = :yearData AND month < :monthData")
    fun deleteBeforeByMonth(yearData: Int, monthData: Int)
    @Query("DELETE FROM contents WHERE year > :yearData")
    fun deleteAfterByYear(yearData: Int)
    @Query("DELETE FROM contents WHERE year = :yearData AND month > :monthData")
    fun deleteAfterByMonth(yearData: Int, monthData: Int)

    @Query("DELETE FROM contents WHERE year = :yearData AND month = :monthData")
    fun deleteYearMonth(yearData: Int, monthData: Int)

    @Query("DELETE FROM contents WHERE year = -1 AND month =:monthData")
    fun deleteMonthOnly(monthData: Int)

    @Insert
    fun insertAll(vararg contents: DataContent)

    @Delete
    fun delete(content: DataContent)
}
