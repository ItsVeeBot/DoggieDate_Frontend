package net.veebot.doggiedate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DogDetailAdapter(private val list: List<DetailViewModel>):
    RecyclerView.Adapter<DogDetailAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val iconView: ImageView = itemView.findViewById(R.id.detailIcon)
        val descView: TextView = itemView.findViewById(R.id.detailDesc)
        val entryView: TextView = itemView.findViewById(R.id.detailEntry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = list[position]
        holder.iconView.setImageResource(itemViewModel.icon)
        holder.descView.text = itemViewModel.desc
        holder.entryView.text = itemViewModel.entry
    }
}