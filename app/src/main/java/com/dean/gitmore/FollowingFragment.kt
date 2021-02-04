package com.dean.gitmore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    companion object {
        val TAG = FollowingFragment::class.java.simpleName
        const val EXTRA_DATA = "extra_data"
    }

    private var listUser: ArrayList<UserData> = ArrayList()
    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowingAdapter(listUser)

        followingViewModel = ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)
        val dataUser = requireActivity().intent.getParcelableExtra<UserData>(EXTRA_DATA) as UserData

        followingViewModel.getUserFollowing(requireActivity().applicationContext, dataUser.username.toString())
        showLoading(true)
        showRecyclerList()
        followingViewModel.getListUsers().observe(requireActivity(), Observer { listFollower ->
            if (listFollower != null) {
                adapter.setData(listFollower)
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList() {
        recycleViewFollowing.layoutManager = LinearLayoutManager(activity)
        val listDataAdapter =
            FollowingAdapter(listUser)
        recycleViewFollowing.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object :
            FollowingAdapter.OnItemClickCallback {
            override fun onItemClicked(UserData: UserData) {
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFollowing.visibility = View.VISIBLE
        } else {
            progressBarFollowing.visibility = View.INVISIBLE
        }
    }
}