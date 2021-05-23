package com.rohan.solutiondevelopersassignment.Authentication

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.rohan.solutiondevelopersassignment.HomeActivity
import com.rohan.solutiondevelopersassignment.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        val myFont: Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.myfont)
        tvLogin.typeface = myFont
        auth = FirebaseAuth.getInstance()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        btnLogin.setOnClickListener {
            val email = etEmailLogin.text.toString()
            val password = etPasswordLogin.text.toString()
            if(email.isEmpty()||password .isEmpty()){
                Toast.makeText(this,"Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            pbLogin.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Toast.makeText(this,"Logged In Successfully!",Toast.LENGTH_SHORT).show()
                    Intent(this, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(it)
                    }
                    pbLogin.visibility = View.INVISIBLE
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                    pbLogin.visibility = View.INVISIBLE
                }
        }
        tvNewUser.setOnClickListener {
            Intent(this,RegisterActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
            }
        }
    }
}