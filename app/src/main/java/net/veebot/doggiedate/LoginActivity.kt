package net.veebot.doggiedate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : Activity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var gotoRegister: TextView

    private lateinit var auth: FirebaseAuth


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser!=null){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editTextEmail = findViewById(R.id.email)
        editTextPass = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressBar)
        gotoRegister = findViewById(R.id.goto_Register)

        auth = Firebase.auth

        gotoRegister.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            var email: String = editTextEmail.text.toString()
            var password: String = editTextPass.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "All fields are required",
                    Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
            else{
                   auth.signInWithEmailAndPassword(email, password)
                       .addOnCompleteListener(this){ task ->
                           if(task.isSuccessful){
                               Log.d("Login", "signInWithEmail: Success")
                               val user = auth.currentUser
                               val intent = Intent(applicationContext, MainActivity::class.java)
                               startActivity(intent)
                               finish()
                           }
                           else{
                               Log.w("Login", "signInWithEmail: Fail", task.exception)
                               Toast.makeText(this, "Authentication Failed",
                                   Toast.LENGTH_SHORT).show()

                           }
                           progressBar.visibility = View.GONE
                       }
            }
        }
    }


}