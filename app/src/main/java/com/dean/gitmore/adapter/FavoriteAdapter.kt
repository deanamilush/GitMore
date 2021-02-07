package com.dean.gitmore.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dean.gitmore.R
import com.dean.gitmore.global.DetailUserActivity
import com.dean.gitmore.global.FavoriteActivity
import com.dean.gitmore.model.Favorite
import com.dean.gitmore.model.UserData
import kotlinx.android.synthetic.main.user_item.view.*
import java.util.ArrayList

class FavoriteAdapter(favoriteActivity: FavoriteActivity) : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>()  {

    var listFavorite = ArrayList<Favorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavorite[position])

        val data = listFavorite[position]
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
            val mIntent = Intent(it.context, DetailUserActivity::class.java)
            mIntent.putExtra(DetailUserActivity.EXTRA_DATA, dataUser)
            it.context.startActivity(mIntent)
        }
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(fav: Favorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(fav.avatar)
                    .apply(RequestOptions().override(250, 250))
                    .into(itemView.img_avatar)
                txt_username.text = fav.username
                txt_name.text = fav.name
            }
        }
    }
}