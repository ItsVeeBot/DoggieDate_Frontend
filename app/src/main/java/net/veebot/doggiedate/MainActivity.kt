package net.veebot.doggiedate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : Activity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var logoutButton: Button
    private lateinit var userInfoText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        logoutButton = findViewById(R.id.btn_logout)
        userInfoText = findViewById(R.id.user_details)
        val currentUser = auth.currentUser

        if(currentUser == null){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            userInfoText.text = getString(R.string.hello, currentUser.email)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}