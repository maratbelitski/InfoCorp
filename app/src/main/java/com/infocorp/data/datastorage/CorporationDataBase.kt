package com.infocorp.data.datastorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.data.corporationdto.OldCorporationsDto

@Database(entities = [CorporationDto::class, FavouriteCorporationsDto::class, OldCorporationsDto::class],
    version = 1, exportSchema = true)
abstract class CorporationDataBase : RoomDatabase() {
    abstract fun getDaoCorp(): CorporationDao
    abstract fun getDaoNewCorps(): OldCorpDao
    abstract fun getDaoFavourite(): FavouriteDao


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