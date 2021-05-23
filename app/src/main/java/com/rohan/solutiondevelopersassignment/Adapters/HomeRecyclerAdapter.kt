package com.rohan.solutiondevelopersassignment.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohan.solutiondevelopersassignment.Model.User
import com.rohan.solutiondevelopersassignment.R

class HomeRecyclerAdapter(val list:ArrayList<User>,private val listener: HomeItemClicked):RecyclerView.Adapter<HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_single_row,parent,false)
        val vh = HomeViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(list[vh.adapterPosition])
        }
        return vh
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val user = list[position]
        holder.name.text = user.name.toString()
        holder.age.text = user.age.toString()
        holder.dob.text = user.dob.toString()
        holder.uid.text = user.uid.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val name = itemView.findViewById<TextView>(R.id.tvNameRecVal)
    val age = itemView.findViewById<TextView>(R.id.tvAgeRecVal)
    val dob = itemView.findViewById<TextView>(R.id.tvDobRecVal)
    val uid = itemView.findViewById<TextView>(R.id.uidRec)
}
interface HomeItemClicked{
    fun onItemClicked(item:User)
}