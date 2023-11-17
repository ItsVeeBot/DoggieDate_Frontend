package net.veebot.doggiedate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DogListAdapter(private val dogList: List<ItemsViewModel>): RecyclerView.Adapter<DogListAdapter.ViewHolder>(){
  class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
      val imageView: ImageView = itemView.findViewById(R.id.imageView)
      val dogNameView: TextView = itemView.findViewById(R.id.dogName)
      val dogBreedView: TextView = itemView.findViewById(R.id.dogBreed)
  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dog, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dogList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = dogList[position]
        holder.imageView.setImageResource(itemsViewModel.image)
        holder.dogNameView.text = itemsViewModel.dogName
        holder.dogBreedView.text = itemsViewModel.dogBreed
    }

}