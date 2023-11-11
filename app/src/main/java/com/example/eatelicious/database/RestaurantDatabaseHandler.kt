package com.example.eatelicious.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.eatelicious.models.Restaurant

class RestaurantDatabaseHandler(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "restaurant_db"
        private const val DB_VERSION = 1

        private object RestaurantRecord : BaseColumns {
            const val TABLE_NAME = "restaurants"
            const val COL_NAME = "name"
            const val COL_RATING = "rating"
            const val COL_IMAGE_URL = "image"
            const val COL_IS_ACTIVE = "is_active"
        }

    }

    override fun onCreate(db: SQLiteDatabase) {

        val createQuery = "CREATE TABLE ${RestaurantRecord.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${RestaurantRecord.COL_NAME}  TEXT, " +
                "${RestaurantRecord.COL_RATING} REAL, " +
                "${RestaurantRecord.COL_IMAGE_URL} TEXT, " +
                "${RestaurantRecord.COL_IS_ACTIVE} INTEGER)"

        db.execSQL(createQuery)
    }


    fun getRestaurant(id: Int): Restaurant {
        val db = this.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            RestaurantRecord.COL_NAME,
            RestaurantRecord.COL_RATING,
            RestaurantRecord.COL_IS_ACTIVE,
            RestaurantRecord.COL_IMAGE_URL)

        // Filter results on restaurant ID
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf("$id")

        val cursor = db.query(
            RestaurantRecord.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        lateinit var restaurant: Restaurant

        with (cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(
                    RestaurantRecord.COL_NAME))
                val rating = getFloat(getColumnIndexOrThrow(
                    RestaurantRecord.COL_RATING))
                val imageUrl = getString(getColumnIndexOrThrow(
                    RestaurantRecord.COL_IMAGE_URL))
                val isRestaurantActive = getInt(getColumnIndexOrThrow(
                    RestaurantRecord.COL_IS_ACTIVE))

                restaurant = Restaurant(
                    id, name, rating, imageUrl, isRestaurantActive == 1)

                break
            }
        }

        cursor.close()

        return restaurant
    }

    fun getRestaurants(isActive: Boolean): List<Restaurant> {
        val db = this.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            RestaurantRecord.COL_NAME,
            RestaurantRecord.COL_RATING,
            RestaurantRecord.COL_IS_ACTIVE,
            RestaurantRecord.COL_IMAGE_URL)

        // Filter results on whether restaurants are active
        val selection = if (isActive)
            "${RestaurantRecord.COL_IS_ACTIVE} = ?"
        else
            null
        val selectionArgs = if (isActive) arrayOf("1") else null

        val sortOrder = "${RestaurantRecord.COL_RATING} DESC"

        val cursor = db.query(
            RestaurantRecord.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )

        val restaurantList = mutableListOf<Restaurant>()

        with (cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(
                    BaseColumns._ID))
                val name = getString(getColumnIndexOrThrow(
                    RestaurantRecord.COL_NAME))
                val rating = getFloat(getColumnIndexOrThrow(
                    RestaurantRecord.COL_RATING))
                val imageUrl = getString(getColumnIndexOrThrow(
                    RestaurantRecord.COL_IMAGE_URL))
                val isRestaurantActive = getInt(getColumnIndexOrThrow(
                    RestaurantRecord.COL_IS_ACTIVE))

                val restaurant = Restaurant(
                    id, name, rating, imageUrl, isRestaurantActive == 1)
                restaurantList.add(restaurant)
            }
        }

        cursor.close()

        return restaurantList
    }

    fun addRestaurant(restaurant: Restaurant) {

        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(RestaurantRecord.COL_NAME, restaurant.name)
            put(RestaurantRecord.COL_RATING, restaurant.rating)
            put(RestaurantRecord.COL_IMAGE_URL, restaurant.imageUrl)
            put(RestaurantRecord.COL_IS_ACTIVE, restaurant.isActive)
        }

        db.insert(RestaurantRecord.TABLE_NAME, null, values)

        db.close()
    }

    fun editRestaurant(restaurant: Restaurant) {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(RestaurantRecord.COL_NAME, restaurant.name)
            put(RestaurantRecord.COL_RATING, restaurant.rating)
            put(RestaurantRecord.COL_IMAGE_URL, restaurant.imageUrl)
            put(RestaurantRecord.COL_IS_ACTIVE, restaurant.isActive)
        }

        // Which row to update, based on the id
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf("${restaurant.id}")

        db.update(RestaurantRecord.TABLE_NAME, values, selection, selectionArgs)

        db.close()
    }

    fun deleteRestaurant(id: Int) {
        val db = this.writableDatabase

        // Which row to update, based on the id
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf("$id")

        db.delete(RestaurantRecord.TABLE_NAME, selection, selectionArgs)

        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is usually a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS $RestaurantRecord.TABLE_NAME")

        onCreate(db)
    }
}
