package com.infocorp.data.datastorage

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.data.corporationdto.OldCorporationsDto
import com.infocorp.data.corporationdto.ResumeStateDto
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.utils.Constants.DATABASE_NAME


@Database(
    entities =
    [CorporationDto::class,
        FavouriteCorporationsDto::class,
        OldCorporationsDto::class,
        UserCorporationDto::class,
        ResumeStateDto::class],
    version = 1,
//    autoMigrations = [
//        AutoMigration(
//            from = 1,
//            to = 2
//        ),
//        AutoMigration(
//            from = 3,
//            to = 4
//        ),
//        AutoMigration(
//            from = 4,
//            to = 5
//        ),
//        AutoMigration(
//            from = 5,
//            to = 6
//        ),
//    ],
    exportSchema = true
)
abstract class CorporationDataBase : RoomDatabase() {
    abstract fun getDaoCorp(): CorporationDao
    abstract fun getDaoUserCorp(): UserCorporationDao
    abstract fun getDaoNewCorps(): OldCorpDao
    abstract fun getDaoFavourite(): FavouriteDao
    abstract fun getDaoResume(): ResumeStateDao

    companion object {

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
                       // .addMigrations(MIGRATION_2_3)
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

//val MIGRATION_2_3 = object : Migration(2, 3) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        db.execSQL( "CREATE TABLE IF NOT EXISTS `resumeStateTable` (`id` TEXT NOT NULL, `idCorporation` TEXT NOT NULL, `poster` TEXT NOT NULL, `title` TEXT NOT NULL, `dateSent` TEXT NOT NULL, `dateResponse` TEXT NOT NULL, `result` INTEGER NOT NULL, `notes` TEXT NOT NULL, PRIMARY KEY(`id`))")
//    }
//}

