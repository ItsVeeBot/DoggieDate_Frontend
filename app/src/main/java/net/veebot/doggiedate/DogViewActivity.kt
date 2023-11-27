package net.veebot.doggiedate


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage

class DogViewActivity : AppCompatActivity() {

    private val dogDocument = FirebaseFirestore.getInstance().collection("publicdata/readwrite/dogs")
    private val detailsList = ArrayList<DetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_view)
        supportActionBar?.title = "Doggo!"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val recycler = findViewById<RecyclerView>(R.id.detail_recycler)
        recycler.layoutManager = LinearLayoutManager(this)

        val entryMap = mapOf(
        "birthdate" to DetailTemplate(getString(R.string.detail_birthdate), R.drawable.detail_birthday),
        "breeds" to DetailTemplate(getString(R.string.detail_breeds), R.drawable.detail_breeds),
        "coat" to DetailTemplate(getString(R.string.detail_coat), R.drawable.detail_coat),
        "female" to DetailTemplate(getString(R.string.detail_sex), R.drawable.detail_female),
        "houseTrained" to DetailTemplate(getString(R.string.detail_housetrained), R.drawable.detail_housetrained),
        "interactions" to DetailTemplate(getString(R.string.detail_interactions), R.drawable.detail_interactions),
        "specialNeeds" to DetailTemplate(getString(R.string.detail_specialNeeds), R.drawable.detail_special_needs),
        "lbs" to DetailTemplate(getString(R.string.detail_weight), R.drawable.detail_weight),
        )

        val documentID = intent.getStringExtra("ITEM")
        val doc = documentID?.let { dogDocument.document(it) }
        val adapter = DogDetailAdapter(detailsList)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        doc?.get()?.addOnSuccessListener { entries ->
            val beforeCount = detailsList.size
            if(entries.data != null){
                for(entry in entries.data!!){
                    if(entry.key == "name"){
                        supportActionBar?.title = entry.value as String
                    }
                    else if (entry.key=="link" &&
                        (entry.value.toString().startsWith("http://") || entry.value.toString().startsWith("https://"))){
                        val fab = findViewById<ExtendedFloatingActionButton>(R.id.learnMoreButton)
                        fab.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(entry.value.toString()))
                            startActivity(intent)
                        }
                        fab.visibility = View.VISIBLE
                    }
                    else if (entry.key=="image"){
                        val storage = Firebase.storage
                        if(entry.value != null && entry.value!=""){
                            val image = storage.getReferenceFromUrl(entry.value.toString())
                            image.downloadUrl.addOnSuccessListener { Uri ->
                                val imageView = findViewById<ImageView>(R.id.dogImg)
                                Glide.with(this).load(Uri.toString()).placeholder(R.drawable.ic_fab_more_info)
                                    .into(imageView)
                            }
                        }
                    }
                    else{
                        val entryTemplate = entryMap.withDefault { DetailTemplate(entry.key, R.drawable.detail_default) }
                            .getValue(entry.key)
                        //TODO: Parse non-string entries
                        val detailEntry = DetailViewModel(entryTemplate.icon, entryTemplate.desc, entry.value.toString())
                        if(entry.value is Boolean){
                            if(entry.key=="female"){
                                if(entry.value as Boolean){
                                    detailEntry.entry = "Female"
                                }
                                else{
                                    detailEntry.entry = "Male"
                                    detailEntry.icon = R.drawable.detail_male
                                }
                            }
                            else if(entry.value as Boolean){
                                detailEntry.entry = "Yes"
                            }
                            else{
                                detailEntry.entry = "No"
                            }
                        }
                        else if(entry.value is Timestamp){
                            //TODO: Parse dates. Gave up on it for now.
                        }

                        detailsList.add(detailEntry)
                    }
                }
                recycler.adapter?.notifyItemRangeInserted(beforeCount, detailsList.size)
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }

}