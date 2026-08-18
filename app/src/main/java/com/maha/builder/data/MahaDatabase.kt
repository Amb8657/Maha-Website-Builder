package com.maha.builder.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Project::class], version = 1, exportSchema = false)
abstract class MahaDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    companion object {
        @Volatile private var INSTANCE: MahaDatabase? = null
        fun getDatabase(context: Context): MahaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MahaDatabase::class.java,
                    "maha_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
