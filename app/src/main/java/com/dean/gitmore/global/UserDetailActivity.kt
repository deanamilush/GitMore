package com.dean.gitmore.global

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dean.gitmore.R
import com.dean.gitmore.adapter.SectionsPagerAdapter
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.FAVORITE
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.dean.gitmore.db.UserHelper
import com.dean.gitmore.model.UserData
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var dbHelper: UserHelper
    private var statusFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = UserHelper.getInstance(applicationContext)
        dbHelper.open()

        val usernameVal = intent.getParcelableExtra<UserData>(EXTRA_DATA) as UserData
        val cursor: Cursor = dbHelper.queryByUsername(usernameVal.username.toString())
        if (cursor.moveToNext()) {
            statusFavorite = true
            setStatusFavorite(true)
        }

        if (supportActionBar != null) {
            supportActionBar?.title = getString(R.string.detail_user)
        }

        setData()
        fab_favorite.setOnClickListener(this)

        setData()
        viewPagerConfig()
    }

    private fun setStatusFavorite(status: Boolean) {
        if (status) {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            fab_favorite.setImageResource(R.drawable.ic_baseline_unfavorite_24)
        }
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }


    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    private fun setData() {
        val dataUser = intent.getParcelableExtra<UserData>(EXTRA_DATA) as UserData
        dataUser.name?.let { setActionBarTitle(it) }
        detail_name.text = dataUser.name
        detail_username.text = dataUser.username
        detail_company.text = dataUser.company
        detail_location.text = dataUser.location
        user_followers.text = dataUser.followers
        user_following.text = dataUser.following
        detail_repository.text = dataUser.repository
        Glide.with(this)
            .load(dataUser.avatar)
            .into(detail_avatar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        val data = intent.getParcelableExtra<UserData>(EXTRA_DATA) as UserData
        when (v?.id) {
            R.id.fab_favorite -> {
                if (statusFavorite) {
                    val idUser = data.username.toString()
                    dbHelper.deleteById(idUser)
                    Toast.makeText(this, "Data Deleted from Favorite", Toast.LENGTH_SHORT).show()
                    setStatusFavorite(false)
                    statusFavorite = true
                } else {

                    val values = ContentValues()
                    values.put(USERNAME, data.username)
                    values.put(NAME, data.name)
                    values.put(AVATAR, data.avatar)
                    values.put(COMPANY, data.company)
                    values.put(LOCATION, data.location)
                    values.put(REPOSITORY, data.repository)
                    values.put(FOLLOWERS, data.followers)
                    values.put(FOLLOWING, data.following)
                    values.put(FAVORITE, "isFav")

                    statusFavorite = false
                //    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(this, "Data Added to Favorite", Toast.LENGTH_SHORT).show()
                    setStatusFavorite(true)
                }
            }
        }
    }

}