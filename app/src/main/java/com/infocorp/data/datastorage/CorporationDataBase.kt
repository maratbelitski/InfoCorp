package com.infocorp.data.datastorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.infocorp.data.corporationdto.CorporationDto

@Database(entities = [CorporationDto::class], version = 1, exportSchema = true)
abstract class CorporationDataBase : RoomDatabase() {
    abstract fun getDao(): CorporationDao

    companion object {
        private const val Database_NAME = "corporation.db"

        @Volatile
        private var INSTANCE: CorporationDataBase? = null

        fun getInstance(context: Context): CorporationDataBase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CorporationDataBase::class.java,
                        Database_NAME
                    )
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}