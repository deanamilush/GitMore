package com.dean.consumerapp.global

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dean.consumerapp.R
import com.dean.consumerapp.adapter.FavoriteAdapter
import com.dean.consumerapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dean.consumerapp.db.UserMapping
import com.dean.consumerapp.model.Favorite
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycleViewFav.layoutManager = LinearLayoutManager(this)
        recycleViewFav.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        recycleViewFav.adapter = adapter

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

    }

    private fun loadListFavourite() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                UserMapping.mapCursorToArrayList(cursor)
            }
            val favData = deferredFav.await()
            showLoading(false)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    override fun onResume() {
        super.onResume()
        loadListFavourite()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFav.visibility = View.VISIBLE
        } else {
            progressBarFav.visibility = View.INVISIBLE
        }
    }
}