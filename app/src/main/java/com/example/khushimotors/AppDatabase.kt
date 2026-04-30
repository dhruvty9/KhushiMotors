package com.example.khushimotors

import android.content.Context
import androidx.room.*

@Entity(tableName = "booking_table")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceName: String,
    val date: String,
    val status: String
)

@Dao
interface BookingDao {
    @Query("SELECT * FROM booking_table ORDER BY id DESC")
    fun getAllBookings(): kotlinx.coroutines.flow.Flow<List<Booking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(booking: Booking)
}

@Database(entities = [Booking::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "khushi_motors_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
