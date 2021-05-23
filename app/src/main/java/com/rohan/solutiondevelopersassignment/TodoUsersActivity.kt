package com.rohan.solutiondevelopersassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rohan.solutiondevelopersassignment.Adapters.TodoUsersAdapter
import com.rohan.solutiondevelopersassignment.Adapters.TodoUsersOnItemClicked
import com.rohan.solutiondevelopersassignment.Model.Todo
import kotlinx.android.synthetic.main.activity_todo_users.*

class TodoUsersActivity : AppCompatActivity(), TodoUsersOnItemClicked {
    lateinit var uid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_users)
        val name = intent.getStringExtra("name")
        supportActionBar?.title = "$name's Tasks"
        uid = intent.getStringExtra("uid")!!
        adapter = TodoUsersAdapter(list,this)
        rvTodoUsers.layoutManager = LinearLayoutManager(this)
        rvTodoUsers.adapter = adapter
        listenTodos()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    lateinit var adapter:TodoUsersAdapter
    var list = ArrayList<Todo>()
    val dbUrl = "https://solutiondevelopersassignment-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private fun listenTodos(){
        val ref = FirebaseDatabase.getInstance(dbUrl).getReference("todo/$uid")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val todo = snapshot.getValue(Todo::class.java)!!
                list.add(todo)
                adapter.notifyDataSetChanged()
                rvTodoUsers.scrollToPosition(adapter.itemCount-1)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val todo = snapshot.getValue(Todo::class.java)!!
                var i=0
                while(i<list.size){
                    if(list[i].id==todo.id){
                        list.removeAt(i)
                        list.add(i,todo)
                        break
                    }
                    i++
                }
                adapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val todo = snapshot.getValue(Todo::class.java)!!
                var i=0
                while(i<list.size){
                    if(list[i].id==todo.id){
                        list.removeAt(i)
                        break
                    }
                    i++
                }
                adapter.notifyDataSetChanged()
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClicked(item: Todo) {

    }
}