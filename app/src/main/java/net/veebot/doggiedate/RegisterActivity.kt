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

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPass: EditText
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var gotoLogin: TextView

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
        setContentView(R.layout.activity_register)
        editTextEmail = findViewById(R.id.email)
        editTextPass = findViewById(R.id.password)
        btnRegister = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progressBar)
        gotoLogin = findViewById(R.id.goto_Login)

        auth = Firebase.auth

        gotoLogin.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnRegister.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            var email: String = editTextEmail.text.toString()
            var password: String = editTextPass.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "All fields are required",
                    Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
            else{
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful) {
                            Log.d("Login", "createUserWithEmail: success")
                            Toast.makeText(
                                this, "Account created successfully!",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Log.w("Login", "createUserWithEmail: fail", task.exception)
                            Toast.makeText(
                                this, "Account creation failed", Toast.LENGTH_SHORT)
                                .show()

                        }
                        progressBar.visibility = View.GONE
                    }
            }
        }
    }
}