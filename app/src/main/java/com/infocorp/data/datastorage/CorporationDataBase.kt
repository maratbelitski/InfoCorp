package com.infocorp.data.datastorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.data.corporationdto.OldCorporationsDto
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.utils.Constants.DATABASE_NAME


@Database(entities =
    [CorporationDto::class,
    FavouriteCorporationsDto::class,
    OldCorporationsDto::class,
    UserCorporationDto::class],
    version = 1, exportSchema = true)
abstract class CorporationDataBase : RoomDatabase() {
    abstract fun getDaoCorp(): CorporationDao
    abstract fun getDaoUserCorp(): UserCorporationDao
    abstract fun getDaoNewCorps(): OldCorpDao
    abstract fun getDaoFavourite(): FavouriteDao


    companion object {
//        private const val DATABASE_NAME = "corporation.db"

        @Volatile
        private var INSTANCE: CorporationDataBase? = null

        fun getInstance(context: Context): CorporationDataBase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CorporationDataBase::class.java,
                        DATABASE_NAME.value
                    )
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}