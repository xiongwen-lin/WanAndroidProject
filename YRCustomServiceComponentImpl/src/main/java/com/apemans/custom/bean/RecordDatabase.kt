package com.apemans.custom.bean

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * /Users/caro/apeman/ModulesProject/CustomerService/build/tmp/kapt3/stubs/debug/com/apeman/customerservice/database/RecordDatabase.java:7:
 * 警告: Schema export directory is not provided to the annotation processor so we cannot export the schema.
 * You can either provide `room.schemaLocation` annotation processor argument OR set exportSchema to false.
 */
//@Database(entities = [RecordDataBean::class], version = 2, exportSchema = true)
@Database(entities = [RecordDataBean::class], version = 3, exportSchema = false)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun chatRecordDao(): ChatRecordDao

    companion object {
        @JvmStatic
        @Volatile
        private var INSTANCE: RecordDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): RecordDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordDatabase::class.java,
                    "record_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}