package net.veebot.doggiedate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val dogDocument = FirebaseFirestore.getInstance().collection("publicdata/readwrite/dogs")
    private val dogList = ArrayList<ItemsViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var loadTextView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val debugList = ArrayList<ItemsViewModel>()

        val itemAdapter = DogListAdapter(requireContext(), dogList)
        recyclerView = view.findViewById(R.id.listRecycler)
        loadTextView = view.findViewById<LinearLayout>(R.id.loadingLayout)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
        itemAdapter.onItemClick = { item ->
            val intent = Intent(context, DogViewActivity::class.java)
            intent.putExtra("ITEM", item.documentId)
            startActivity(intent)
        }
        refreshDogList()
    }

    fun refreshDogList(){
        val beforeCount = dogList.size
        dogDocument.get().addOnSuccessListener { documents ->
            for (document in documents) {

                val name = document.getString("name")
                if (name != null && name != "") {
                    var breeds = ""
                    val breedsList = document.get("breeds") as List<String>?
                    if(breedsList != null) {
                        var addComma = false
                        for (item in breedsList) {
                            if (addComma) {
                                breeds = breeds.plus(", ")
                            }
                            breeds = breeds.plus(item)
                            addComma = true
                        }
                    }
                    val storage = Firebase.storage
                    var image = storage.reference
                    val imageRef = document.getString("image")
                    if(imageRef!=null && imageRef != ""){
                        image = storage.getReferenceFromUrl(imageRef)
                    }
                    dogList.add(
                        ItemsViewModel(
                            image,
                            name,
                            breeds,
                            document.id
                        )
                    )
                }
            }
            recyclerView.adapter?.notifyItemRangeInserted(beforeCount, dogList.size)
            loadTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}