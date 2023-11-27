package net.veebot.doggiedate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DogListAdapter(private val context: Context, private val dogList: List<ItemsViewModel>): RecyclerView.Adapter<DogListAdapter.ViewHolder>(){

    var onItemClick: ((ItemsViewModel) -> Unit)? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val dogNameView: TextView = itemView.findViewById(R.id.dogName)
        val dogBreedView: TextView = itemView.findViewById(R.id.dogBreed)
        init{
            itemView.setOnClickListener{
                onItemClick?.invoke(dogList[bindingAdapterPosition])
            }
        }
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
        // TODO: Replace this with the actual image from the object
        if(itemsViewModel.image != Firebase.storage.reference) {
            itemsViewModel.image.downloadUrl.addOnSuccessListener { Uri ->
                Glide.with(context).load(Uri.toString()).placeholder(R.drawable.ic_fab_more_info)
                    .into(holder.imageView)
            }
        }
        else holder.imageView.setImageResource(R.drawable.ic_fab_more_info)
        holder.dogNameView.text = itemsViewModel.dogName
        holder.dogBreedView.text = itemsViewModel.dogBreed
    }

}