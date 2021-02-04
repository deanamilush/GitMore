package com.dean.gitmore.db

import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract {

    companion object {
        const val AUTHORITY = "com.dean.gitmore"
        const val SCHEME = "content"
    }

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val FAVORITE = "isFav"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(
                TABLE_NAME
            ).build()
        }
    }
}