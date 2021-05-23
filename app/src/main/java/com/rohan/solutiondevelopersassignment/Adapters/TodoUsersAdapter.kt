package com.rohan.solutiondevelopersassignment.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohan.solutiondevelopersassignment.Model.Todo
import com.rohan.solutiondevelopersassignment.R

class TodoUsersAdapter(val list:ArrayList<Todo>,private val listener:TodoUsersOnItemClicked):RecyclerView.Adapter<TodoUsersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoUsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_users_row,parent,false)
        val vh = TodoUsersViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(list[vh.adapterPosition])
        }
        return vh
    }
    override fun onBindViewHolder(holder: TodoUsersViewHolder, position: Int) {
        val todo = list[position]
        holder.title.text = todo.title.toString()
        holder.description.text = todo.description.toString()
        holder.id.text = todo.id.toString()
    }
    override fun getItemCount(): Int {
        return list.size
    }
}
class TodoUsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val title = itemView.findViewById<TextView>(R.id.tvTodoTitleUsers)
    val description = itemView.findViewById<TextView>(R.id.tvTodoDescUsers)
    val id = itemView.findViewById<TextView>(R.id.tvTodoUsersId)
}
interface TodoUsersOnItemClicked{
    fun onItemClicked(item: Todo)
}