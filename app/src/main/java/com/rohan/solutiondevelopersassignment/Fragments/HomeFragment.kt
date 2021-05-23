package com.rohan.solutiondevelopersassignment.Fragments

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rohan.solutiondevelopersassignment.Adapters.HomeItemClicked
import com.rohan.solutiondevelopersassignment.Adapters.HomeRecyclerAdapter
import com.rohan.solutiondevelopersassignment.Model.User
import com.rohan.solutiondevelopersassignment.R
import com.rohan.solutiondevelopersassignment.TodoSelfActivity
import com.rohan.solutiondevelopersassignment.TodoUsersActivity
import kotlinx.coroutines.*

class HomeFragment : Fragment(), HomeItemClicked {
    var list = ArrayList<User>()
    val dbUrl = "https://solutiondevelopersassignment-default-rtdb.asia-southeast1.firebasedatabase.app/"
    lateinit var adapter: HomeRecyclerAdapter
    lateinit var uid:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val rvHomeFrag = view.findViewById<RecyclerView>(R.id.rvHomeFrag)
        val cvHomeFrag = view.findViewById<CardView>(R.id.cvHomeFrag)
        view.findViewById<ProgressBar>(R.id.pbHome).visibility = View.VISIBLE
        rvHomeFrag.visibility = View.INVISIBLE
        cvHomeFrag.visibility = View.INVISIBLE
        uid = FirebaseAuth.getInstance().uid.toString()
        list.clear()
        rvHomeFrag.layoutManager = LinearLayoutManager(context)
        adapter = HomeRecyclerAdapter(list,this)
        rvHomeFrag.adapter = adapter
        fetchUsers()
        Handler().postDelayed({
            view.findViewById<ProgressBar>(R.id.pbHome).visibility = View.INVISIBLE
            rvHomeFrag.visibility = View.VISIBLE
            cvHomeFrag.visibility = View.VISIBLE
        },1100)
        cvHomeFrag.setOnClickListener {
            Intent(context,TodoSelfActivity::class.java).also {
                startActivity(it)
            }
        }
        return view
    }

    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance(dbUrl).getReference("/users")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)!!
                if (user.uid!=uid) {
                    list.add(user)
                    adapter.notifyDataSetChanged()
                }else if(user.uid==uid){
                    view?.findViewById<TextView>(R.id.tvCurrNameVal)?.text = user.name.toString()
                    view?.findViewById<TextView>(R.id.tvCurrAgeVal)?.text = user.age.toString()
                    view?.findViewById<TextView>(R.id.tvCurrDobVal)?.text = user.dob.toString()
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message.toString(),Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClicked(item: User) {
        Intent(context,TodoUsersActivity::class.java).also {
            it.putExtra("uid",item.uid.toString())
            it.putExtra("name",item.name.toString())
            startActivity(it)
        }
    }
}