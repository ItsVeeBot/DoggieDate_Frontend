package net.veebot.doggiedate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

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

        val itemAdapter = DogListAdapter(dogList)
        recyclerView = view.findViewById(R.id.listRecycler)
        loadTextView = view.findViewById<LinearLayout>(R.id.loadingLayout)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
        refreshDogList()
    }

    fun refreshDogList(){
        val beforeCount = dogList.size
        dogDocument.get().addOnSuccessListener { documents ->
            for (document in documents) {
                var name = document.getString("name")
                if (name != null && name != "") {
                    var breeds = ""
                    var breedsList = document.get("breeds") as List<String>?
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
                    dogList.add(
                        ItemsViewModel(
                            R.color.purple_500,
                            name,
                            breeds,
                            document
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