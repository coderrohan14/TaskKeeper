package com.rohan.solutiondevelopersassignment.Fragments

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rohan.solutiondevelopersassignment.Authentication.LoginActivity
import com.rohan.solutiondevelopersassignment.Model.User
import com.rohan.solutiondevelopersassignment.R

class ProfileFragment : Fragment() {
    val dbUrl = "https://solutiondevelopersassignment-default-rtdb.asia-southeast1.firebasedatabase.app/"
    lateinit var uid:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val myFont: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.myfont2)
        val myFont2: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.myfont)
        view.findViewById<TextView>(R.id.tvNameProfile).typeface = myFont
        view.findViewById<TextView>(R.id.tvAgeProfile).typeface = myFont
        view.findViewById<TextView>(R.id.tvDobProfile).typeface = myFont
        view.findViewById<TextView>(R.id.tvNameProfileH).typeface = myFont2
        view.findViewById<TextView>(R.id.tvAgeProfileH).typeface = myFont2
        view.findViewById<TextView>(R.id.tvDobProfileH).typeface = myFont2
        uid = FirebaseAuth.getInstance().uid.toString()
        val ref = FirebaseDatabase.getInstance(dbUrl).getReference("users/$uid")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                view.findViewById<TextView>(R.id.tvNameProfile).text = user?.name.toString()
                view.findViewById<TextView>(R.id.tvAgeProfile).text = user?.age.toString()
                view.findViewById<TextView>(R.id.tvDobProfile).text = user?.dob.toString()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        view.findViewById<Button>(R.id.btnLogOut).setOnClickListener {
            val alert = AlertDialog.Builder(requireContext())
            alert.setTitle("Log Out?")
            alert.setMessage("Are you sure you want to Log Out?")
            alert.setPositiveButton("Yes") { text, listener ->
                FirebaseAuth.getInstance().signOut()
                Intent(requireContext(), LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(it)
                }
            }
            alert.setNegativeButton("No") { text, listener ->

            }
            alert.create()
            alert.show()
        }
        return view
    }
}