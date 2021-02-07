package com.dean.gitmore.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dean.gitmore.R
import com.dean.gitmore.model.UserData
import com.dean.gitmore.global.DetailUserActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_item.view.*
import kotlin.collections.ArrayList

class UserAdapter(private var listData: ArrayList<UserData>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>(){
    lateinit var mcontext: Context

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserData>) {
        listData.clear()
        listData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_item, viewGroup, false)
        val sch = ListViewHolder(view)
        mcontext = viewGroup.context
        return sch
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        Glide.with(holder.itemView.context)
            .load(data.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.imgAvatar)
        holder.txtUsername.text = data.username
        holder.txtName.text = data.name
        holder.itemView.setOnClickListener {
            val dataUser = UserData(
                data.username,
                data.name,
                data.avatar,
                data.company,
                data.location,
                data.repository,
                data.followers,
                data.following
            )
            val intentDetail = Intent(mcontext, DetailUserActivity::class.java)
            intentDetail.putExtra(DetailUserActivity.EXTRA_DATA, dataUser)
            mcontext.startActivity(intentDetail)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: UserData)
    }

    override fun getItemCount(): Int = listData.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: CircleImageView = itemView.img_avatar
        var txtUsername: TextView = itemView.txt_username
        var txtName: TextView = itemView.txt_name
    }
}