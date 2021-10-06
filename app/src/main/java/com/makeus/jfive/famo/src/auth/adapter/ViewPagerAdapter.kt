package com.makeus.jfive.famo.src.auth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.R

class ViewPagerAdapter(private var title:List<String>,private var details:List<String>,private var images:List<Int>):RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val itemTitle:TextView = itemView.findViewById(R.id.information_title)
        val itemDetails : TextView = itemView.findViewById(R.id.information_description)
        val itemImage : ImageView = itemView.findViewById(R.id.information_image)
        init {
            itemImage.setOnClickListener {
                v:View->
                val position = adapterPosition
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_information,parent,false))
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemDetails.text = details[position]
        holder.itemImage.setImageResource(images[position])

    }

    override fun getItemCount(): Int  = title.size

}