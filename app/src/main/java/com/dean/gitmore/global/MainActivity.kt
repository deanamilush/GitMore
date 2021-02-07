package com.dean.gitmore.global

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dean.gitmore.R
import com.dean.gitmore.adapter.UserAdapter
import com.dean.gitmore.databinding.ActivityMainBinding
import com.dean.gitmore.model.UserData
import com.dean.gitmore.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var listData: ArrayList<UserData> = ArrayList()
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycleView.setHasFixedSize(true)
        adapter = UserAdapter(listData)

        recycleView.layoutManager = LinearLayoutManager(recycleView.context)
        recycleView.setHasFixedSize(true)
        recycleView.addItemDecoration(
            DividerItemDecoration(recycleView.context, DividerItemDecoration.VERTICAL)
        )

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(MainViewModel::class.java)
        configViewModel(adapter)

        searchUser()
        showList()
        getUser()
    }

    private fun searchUser() {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isNotEmpty()) {
                listData.clear()
                showList()
                mainViewModel.getUserSearch(query, applicationContext)
                showLoading(true)
                configViewModel(adapter)
            } else {
             return true
            }
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    })
    }

    private fun getUser() {
        mainViewModel.getUser(applicationContext)
        showLoading(true)
    }

    private fun showList() {
        recycleView.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = UserAdapter(listData)
        recycleView.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(dataUsers: UserData) {
                showDetailUser(dataUsers)
            }
        })
    }

    private fun configViewModel(adapter: UserAdapter) {
        mainViewModel.getListUsers().observe(this, Observer { listUsers ->
            if (listUsers != null) {
                adapter.setData(listUsers)
                showLoading(false)
            }
        })
    }

    private fun showDetailUser(dataUser: UserData) {
        UserData(
            dataUser.username,
            dataUser.name,
            dataUser.avatar,
            dataUser.company,
            dataUser.location,
            dataUser.repository,
            dataUser.followers,
            dataUser.following
        )
        val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_DATA, dataUser)

        this@MainActivity.startActivity(intent)
        Toast.makeText(
            this,
            "${dataUser.name}",
            Toast.LENGTH_SHORT ).show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                val move = Intent(this, FavoriteActivity::class.java)
                startActivity(move)
            }
            R.id.setting_menu -> {
                val move = Intent(this, SettingsActivity::class.java)
                startActivity(move)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}