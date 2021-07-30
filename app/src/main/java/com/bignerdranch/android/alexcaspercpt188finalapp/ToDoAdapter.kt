package com.bignerdranch.android.alexcaspercpt188finalapp


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.todo_item.view.*

// Class takes a mutable (dynamic) list of ToDoItem class items and returns a type of recycler view adapter that will hold them
// in the recycler view int the main activity xml
class ToDoAdapter(private val todos: MutableList<ToDoItem>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    //Index just for keeping track of last clicked on to-do
    var selectedIndex = 0

    // Part of the recycler view adapter, I give it a view of layout inflater with context that corresponds to the todo_item xml
    // so the recycler can show the views in that xml as one item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        )
    }

    // Binds the data of each to-do item in the todos list  to each of the views
    // in the recycler view's items using the holder
    // this happens every time notifyDataSetChanged() is run
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val currentTodo = todos[position]
        holder.itemView.apply {
            tvTitle.text = currentTodo.title
            cbChecked.isChecked = currentTodo.isChecked
            // onChecked makes sure that the to-do item sets it's isChecked boolean to false
            cbChecked.setOnCheckedChangeListener { _, isChecked ->
                currentTodo.isChecked = !currentTodo.isChecked
            }
            // If an item in the list is clicked set the selected index to the current position
            holder.itemView.setOnClickListener {
                selectedIndex = holder.layoutPosition
                notifyDataSetChanged()
            }
        }
        // This changes the color of the to-do item whose index matches the selectedIndex (the clicked item)
        // to give it the look of being pressed in (probably better ways of making the effect
        if (selectedIndex == position)
            holder.itemView.layout.setBackgroundColor(Color.parseColor("#509E61"))
        else
            holder.itemView.layout.setBackgroundColor(Color.parseColor("#8AF3A1"))
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    // Adds a to-do and tells anyone watching (the todoviewholder) that it was inserted
    // always put in after all the others so its always size - 1
    fun addTodo(todo: ToDoItem) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    // Removes any to-do that is checked and notifies watchers
    fun deleteDone() {
        todos.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }

    // gets the list so the main activity can copy the to-do's data
    fun getTodos(): MutableList<ToDoItem> {
        return todos
    }
}