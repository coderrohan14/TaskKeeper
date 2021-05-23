package com.rohan.solutiondevelopersassignment.Authentication

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rohan.solutiondevelopersassignment.HomeActivity
import com.rohan.solutiondevelopersassignment.R
import com.rohan.solutiondevelopersassignment.Model.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    lateinit var cal:Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val myFont: Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.myfont)
        tvRegister.typeface = myFont
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        auth = FirebaseAuth.getInstance()
        cal = Calendar.getInstance()
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        etDOB.setOnClickListener {
            DatePickerDialog(this@RegisterActivity,dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        btnRegister.setOnClickListener {
            pbRegister.visibility = View.VISIBLE
            loginUser()
        }
        tvAlreadyRegistered.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        etDOB.setText(sdf.format(cal.getTime()))
    }

    private fun loginUser() {
        val name = etNameRegister.text.toString()
        val age = etAge.text.toString()
        val dob = etDOB.text.toString()
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()&&age.isNotEmpty()&&dob.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    auth.signInWithEmailAndPassword(email, password).await()
                    addUser(name, age, dob)
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, e.message.toString(), Toast.LENGTH_LONG).show()
                    pbRegister.visibility = View.INVISIBLE
                }
            }
        }else{
            Toast.makeText(this,"Please enter all the details.", Toast.LENGTH_LONG).show()
            pbRegister.visibility = View.INVISIBLE
        }
    }

    val dbUrl = "https://solutiondevelopersassignment-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private fun addUser(name:String,age:String,dob:String){
        if(auth.uid==null){
            Toast.makeText(this,"User not logged in..",Toast.LENGTH_SHORT).show()
            return
        }
        val uid = auth.uid?:""
        val ref = FirebaseDatabase.getInstance(dbUrl).getReference("users/$uid")
        val user = User(uid,dob,name,age)
        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"Registered Successfully!!",Toast.LENGTH_SHORT).show()
                Intent(this,HomeActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(it)
                }
                pbRegister.visibility = View.INVISIBLE
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                pbRegister.visibility = View.INVISIBLE
                return@addOnFailureListener
            }
    }
}