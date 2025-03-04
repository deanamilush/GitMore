package com.dean.gitmore.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.FAVORITE
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.USERNAME

class DatabaseHelper (context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "final_submission"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME" +
                " ($USERNAME TEXT NOT NULL," +
                " $NAME TEXT NOT NULL," +
                " $AVATAR TEXT NOT NULL," +
                " $COMPANY TEXT NOT NULL," +
                " $LOCATION TEXT NOT NULL," +
                " $REPOSITORY TEXT NOT NULL," +
                " $FOLLOWERS TEXT NOT NULL," +
                " $FOLLOWING TEXT NOT NULL," +
                " $FAVORITE TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}