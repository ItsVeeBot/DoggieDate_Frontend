package net.veebot.doggiedate

import com.google.firebase.firestore.DocumentSnapshot

data class ItemsViewModel(val image: Int, val dogName: String, val dogBreed: String, val documentSnapshot: DocumentSnapshot){

}
