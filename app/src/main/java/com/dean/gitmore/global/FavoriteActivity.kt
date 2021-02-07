package com.dean.gitmore.global

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dean.gitmore.R
import com.dean.gitmore.adapter.FavoriteAdapter
import com.dean.gitmore.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dean.gitmore.db.UserHelper
import com.dean.gitmore.db.UserMapping
import com.dean.gitmore.model.Favorite
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var dbHelper: UserHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        dbHelper = UserHelper.getInstance(applicationContext)
        dbHelper.open()

        recycleViewFav.layoutManager = LinearLayoutManager(this)
        recycleViewFav.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        recycleViewFav.adapter = adapter

        loadListFavourite()

        val handleThread = HandlerThread("DataObserver")
        handleThread.start()
        val handler = Handler(handleThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadListFavourite()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadListFavourite()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }

        if (supportActionBar != null) {
            supportActionBar?.title = getString(R.string.title_menu_favorite)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun loadListFavourite() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBarFav.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                UserMapping.mapCursorToArrayList(cursor)
            }
            val favData = deferredFav.await()
            progressBarFav.visibility = View.INVISIBLE
            if (favData.size > 0) {
                adapter.listFavorite = favData
            } else {
                adapter.listFavorite = ArrayList()
                showSnack()
            }
        }
    }

    private fun showSnack() {
        Snackbar.make(recycleViewFav, "Data Is Null", Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadListFavourite()
    }
}