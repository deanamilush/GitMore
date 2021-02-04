package com.dean.gitmore.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dean.gitmore.R
import com.dean.gitmore.adapter.FollowersAdapter
import com.dean.gitmore.model.UserData
import com.dean.gitmore.viewmodel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {

    companion object {
        val TAG = FollowersFragment::class.java.simpleName
        const val EXTRA_DATA = "extra_data"
    }

    private var listUser: ArrayList<UserData> = ArrayList()
    private lateinit var adapter: FollowersAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowersAdapter(listUser)

        followersViewModel = ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)
        val dataUser = requireActivity().intent.getParcelableExtra<UserData>(EXTRA_DATA) as UserData

        followersViewModel.getUserFollowers(requireActivity().applicationContext, dataUser.username.toString())
        showLoading(true)
        showRecyclerList()
        followersViewModel.getListUsers().observe(requireActivity(), Observer { listFollower ->
            if (listFollower != null) {
                adapter.setData(listFollower)
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList() {
        recycleViewFollowers.layoutManager = LinearLayoutManager(activity)
        val listDataAdapter =
            FollowersAdapter(listUser)
        recycleViewFollowers.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object :
            FollowersAdapter.OnItemClickCallback {
            override fun onItemClicked(UserData: UserData) {
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFollowers.visibility = View.VISIBLE
        } else {
            progressBarFollowers.visibility = View.INVISIBLE
        }
    }
}