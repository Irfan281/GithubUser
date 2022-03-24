package com.irfan.githubuser2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irfan.githubuser2.data.local.FavoriteUser
import com.irfan.githubuser2.databinding.ItemUserBinding
import com.irfan.githubuser2.data.remote.response.ItemsList
import com.irfan.githubuser2.view.UserDetailActivity

class ListUserAdapter(private val users: List<ItemsList>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    companion object {
        const val USERNAME = "username"
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (username, avatarUrl) = users[position]
        Glide.with(holder.itemView).load(avatarUrl).into(holder.binding.imgAvatar)
        holder.binding.tvUsernameId.text = username

        holder.itemView.setOnClickListener {
            val toDetail = Intent(holder.itemView.context, UserDetailActivity::class.java)
            toDetail.putExtra(USERNAME, users[position])
            holder.itemView.context.startActivity(toDetail)
        }
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
}

