package com.rohan.solutiondevelopersassignment

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rohan.solutiondevelopersassignment.Adapters.TodoSelfAdapter
import com.rohan.solutiondevelopersassignment.Adapters.TodoSelfOnItemClicked
import com.rohan.solutiondevelopersassignment.Adapters.TodoSelfOnViewClicked
import com.rohan.solutiondevelopersassignment.Model.Todo
import kotlinx.android.synthetic.main.activity_todo_self.*
import kotlinx.android.synthetic.main.activity_todo_users.*
import java.util.*
import kotlin.collections.ArrayList


class TodoSelfActivity : AppCompatActivity(), TodoSelfOnItemClicked, TodoSelfOnViewClicked {
    lateinit var uid:String
    lateinit var adapter: TodoSelfAdapter
    lateinit var view: View
    var list = ArrayList<Todo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_self)
        supportActionBar?.title = "Your Tasks"
        titleColor = R.color.white
        uid = FirebaseAuth.getInstance().uid!!
        rvTodoSelf.layoutManager = LinearLayoutManager(this)
        adapter = TodoSelfAdapter(list,this,this)
        rvTodoSelf.adapter = adapter
        listenTodos()
        fabTodo.setOnClickListener {
            view = LayoutInflater.from(this).inflate(R.layout.todo_alert,null, false)
            val alert = AlertDialog.Builder(this)
            view.findViewById<TextView>(R.id.tvAlertTitleT).text = "Add a new task.."
            alert.setView(view)
            alert.setPositiveButton("Add") { text, listener ->
                addTodo()
            }
            alert.setNegativeButton("Cancel") { text, listener ->

            }
            alert.create()
            alert.show()
        }
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

    private fun addTodo(){
        val title = view.findViewById<EditText>(R.id.etAlertTitle)
        val description = view.findViewById<EditText>(R.id.etAlertDesc)
        if(title.text.isEmpty()||description.text.isEmpty()){
            Toast.makeText(this,"Please fill all the fields..",Toast.LENGTH_LONG).show()
            return
        }
        val id = UUID.randomUUID().toString()
        val task = Todo(title.text.toString(), description.text.toString(),id)
        val ref = FirebaseDatabase.getInstance(dbUrl).getReference("todo/$uid/$id")
        ref.setValue(task)
                .addOnSuccessListener {
                    Toast.makeText(this, "New Task Added!!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                }
    }

    val dbUrl = "https://solutiondevelopersassignment-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private fun listenTodos(){
        val ref = FirebaseDatabase.getInstance(dbUrl).getReference("todo/$uid")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("testing1", snapshot.toString())
                val todo = snapshot.getValue(Todo::class.java)!!
                list.add(todo)
                adapter.notifyDataSetChanged()
                rvTodoSelf.scrollToPosition(adapter.itemCount-1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val todo = snapshot.getValue(Todo::class.java)!!
                var i = 0
                while (i < list.size) {
                    if (list[i].id == todo.id) {
                        list.removeAt(i)
                        list.add(i, todo)
                        break
                    }
                    i++
                }
                adapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val todo = snapshot.getValue(Todo::class.java)!!
                var i = 0
                while (i < list.size) {
                    if (list[i].id == todo.id) {
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

    private fun deleteTask(item:Todo){
        val ref = FirebaseDatabase.getInstance(dbUrl).getReference("todo/$uid")
        Log.d("testing1",item.id)
        ref.child(item.id).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this,"Task Deleted!",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message.toString(),Toast.LENGTH_LONG).show()
                }
    }

    override fun onItemClicked(item: Todo) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Delete Task?")
        alert.setMessage("Are you sure you want to delete this task?")
        alert.setPositiveButton("Yes"){text,listener->
            deleteTask(item)
        }
        alert.setNegativeButton("No"){text,listener->

        }
        alert.create()
        alert.show()
    }

    override fun onViewClicked(item: Todo) {
        val viewAlert = LayoutInflater.from(this).inflate(R.layout.todo_alert,null, false)
        viewAlert.findViewById<EditText>(R.id.etAlertTitle).setText(item.title.toString())
        viewAlert.findViewById<EditText>(R.id.etAlertDesc).setText(item.description.toString())
        val alert = AlertDialog.Builder(this)
        viewAlert.findViewById<TextView>(R.id.tvAlertTitleT).text = "Edit this task.."
        alert.setView(viewAlert)
        alert.setPositiveButton("Change") { text, listener ->
            val title = viewAlert.findViewById<EditText>(R.id.etAlertTitle)
            val description = viewAlert.findViewById<EditText>(R.id.etAlertDesc)
            if(title.text.isEmpty()||description.text.isEmpty()){
                Toast.makeText(this,"Please fill all the fields..",Toast.LENGTH_LONG).show()
                return@setPositiveButton
            }
            val id = item.id
            val task = Todo(title.text.toString(), description.text.toString(),id)
            val ref = FirebaseDatabase.getInstance(dbUrl).getReference("todo/$uid/$id")
            ref.setValue(task)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Task Changed!!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                    }
        }
        alert.setNegativeButton("Cancel") { text, listener ->

        }
        alert.create()
        alert.show()
    }
}