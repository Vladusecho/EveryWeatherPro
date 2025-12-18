package com.vladusecho.everyweatherpro.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vladusecho.everyweatherpro.data.local.models.CityDbModel

@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase: RoomDatabase() {

    abstract fun favouriteCitiesDao(): FavouriteCitiesDao

    companion object {

        private var instance: FavouriteDatabase? = null
        private val lock = Any()
        private const val DB_NAME = "FavouriteDatabase"

        fun getInstance(context: Context): FavouriteDatabase {

            instance?.let { return it }

            synchronized(lock) {
                instance?.let { return it }

                val db = Room.databaseBuilder(
                    context = context,
                    klass = FavouriteDatabase::class.java,
                    name = DB_NAME
                ).build()

                instance = db
                return db
            }
        }
    }
}