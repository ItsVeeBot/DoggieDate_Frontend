package net.veebot.doggiedate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class LogoutFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var logoutButton: Button
    private lateinit var userInfoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_logout, container, false)
        auth = FirebaseAuth.getInstance()
        logoutButton = v.findViewById(R.id.btn_logout)
        userInfoText = v.findViewById(R.id.user_details)
        val currentUser = auth.currentUser

        if(currentUser == null){
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
            userInfoText.text = getString(R.string.hello, currentUser.email)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        return v
    }
}