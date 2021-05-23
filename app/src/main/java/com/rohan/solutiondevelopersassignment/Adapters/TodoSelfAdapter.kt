package com.rohan.solutiondevelopersassignment.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohan.solutiondevelopersassignment.Model.Todo
import com.rohan.solutiondevelopersassignment.R

class TodoSelfAdapter(val list:ArrayList<Todo>,private val listener:TodoSelfOnItemClicked,private val listenerView:TodoSelfOnViewClicked): RecyclerView.Adapter<TodoSelfViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoSelfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_row,parent,false)
        val vh = TodoSelfViewHolder(view)
        view.findViewById<ImageView>(R.id.imgTodoDel).setOnClickListener {
            listener.onItemClicked(list[vh.adapterPosition])
        }
        view.findViewById<ImageView>(R.id.imgTodoSelfEdit).setOnClickListener {
            listenerView.onViewClicked(list[vh.adapterPosition])
        }
        return vh
    }
    override fun onBindViewHolder(holder: TodoSelfViewHolder, position: Int) {
        val todo = list[position]
        holder.title.text = todo.title.toString()
        holder.description.text = todo.description.toString()
        holder.id.text = todo.id.toString()
    }
    override fun getItemCount(): Int {
        return list.size
    }
}
class TodoSelfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val title = itemView.findViewById<TextView>(R.id.tvTodoTitle)
    val description = itemView.findViewById<TextView>(R.id.tvTodoDesc)
    val id = itemView.findViewById<TextView>(R.id.tvTodoId)
}
interface TodoSelfOnItemClicked{
    fun onItemClicked(item:Todo)
}
interface TodoSelfOnViewClicked{
    fun onViewClicked(item:Todo)
}