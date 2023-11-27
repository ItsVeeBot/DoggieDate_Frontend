package net.veebot.doggiedate


import com.google.firebase.storage.StorageReference

data class ItemsViewModel(val image: StorageReference, val dogName: String, val dogBreed: String, val documentId: String){

}
