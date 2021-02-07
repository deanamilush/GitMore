package com.dean.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dean.consumerapp.R
import com.dean.consumerapp.global.MainActivity
import com.dean.consumerapp.model.Favorite
import kotlinx.android.synthetic.main.user_item.view.*
import java.util.ArrayList

class FavoriteAdapter(mainActivity: MainActivity) : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>()  {


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