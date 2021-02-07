package com.dean.gitmore.db

import android.database.Cursor
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.FAVORITE
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.dean.gitmore.model.Favorite

object UserMapping {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<Favorite> {
        val favList = ArrayList<Favorite>()

        cursor?.apply {
            while (moveToNext()) {
                val username =
                    getString(getColumnIndexOrThrow(USERNAME))
                val name =
                    getString(getColumnIndexOrThrow(NAME))
                val avatar =
                    getString(getColumnIndexOrThrow(AVATAR))
                val company =
                    getString(getColumnIndexOrThrow(COMPANY))
                val location =
                    getString(getColumnIndexOrThrow(LOCATION))
                val repository =
                    getString(getColumnIndexOrThrow(REPOSITORY))
                val followers =
                    getString(getColumnIndexOrThrow(FOLLOWERS))
                val following =
                    getString(getColumnIndexOrThrow(FOLLOWING))
                val isFav =
                    getString(getColumnIndexOrThrow(FAVORITE))

                favList.add(
                    Favorite(username, name, avatar, company, location, repository, followers, following, isFav)
                )
            }
        }
        return favList
    }
}