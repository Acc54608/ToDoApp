// Alex Casper
// CPT-188-A01S
// Final Project
// To-Do List App
package com.bignerdranch.android.alexcaspercpt188finalapp

import android.content.ClipData.newIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // create instance of the adapter class
    private lateinit var todoAdapter: ToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // create shared preferences and give the name the file, if it exists it uses it, otherwise it is created
        val sharedPreferences: SharedPreferences = getSharedPreferences("todo_tasks", MODE_PRIVATE)
        // give the adapter class an empty mutable list since the list will be given a type and items later
        todoAdapter = ToDoAdapter(mutableListOf())

        // tells the recycler view (A fancy version of a list view) that it's adapter is the todoadapter class
        // and that it should display things as a linear layout would
        rvList.adapter = todoAdapter
        rvList.layoutManager = LinearLayoutManager(this)

        // infinite loop to get iterate through every set of data that could be in the shared preferences folder
        // I made i and used a when loop because I could not find a way to make an infinite loop with for
        // and could not find a way to iterate through the elements in the shared preferences folder
        var i = 0
        while (true) {
                // gets a set of strings stored with the key, if there is no set in the shared preferences
                    // then it returns null and breaks the loop
                val strSet = sharedPreferences.getStringSet("Task " + i.toString(), null)
                if (strSet.isNullOrEmpty()) {
                    break
                }
            // the string set isn't ordered s it just gets the 2 strings stored then decides which is stored as the title
            // and which is the note based on whether it has a 1 infront of the string, an arbitrary identifier
            // otherwise some todos would have the note become the name and the name the note
                val strOne = strSet.elementAt(0).toString()
                val strTwo = strSet.elementAt(1).toString()
                if (strOne.startsWith("1", false))
                    todoAdapter.addTodo(ToDoItem(strOne.drop(1), false, strTwo))
                else
                    todoAdapter.addTodo(ToDoItem(strTwo.drop(1), false, strOne))
                i++
        }
        val addButton = findViewById<Button>(R.id.button_add_task)
        val doneButton = findViewById<Button>(R.id.button_delete)
        val noteButton = findViewById<Button>(R.id.button_desc)

        addButton.setOnClickListener {
            // add button creates an alert dialog box with an edit text and button to prompt the user for the name
            // of their to-do
            val alertDialog = AlertDialog.Builder(this)
            val alertEditText = EditText(this)
            alertDialog.setMessage("Enter TODO Name (40 Char Max")
            alertDialog.setTitle("Add TODO")
            alertDialog.setView(alertEditText)
            alertDialog.setPositiveButton("Add"){ alertDialog, _ ->
                // entered name can't be empty or user will be notified and alert box dismissed
                if (alertEditText.text.isEmpty()) {
                    val toast = Toast.makeText(this, "Must name ToDo", Toast.LENGTH_SHORT)
                    toast.show()
                    return@setPositiveButton
                }
                // otherwise edit text is the name and then the to-do is created and added
                // to the list by way of the addtodo function (checked and desc/note aren't needed)
                val todoTitle = alertEditText.text.toString()
                val todoItem = ToDoItem(todoTitle)
                todoAdapter.addTodo(todoItem)
                alertEditText.text.clear()
            }
            alertDialog.show()
        }

        // delete's checked to-do items
        doneButton.setOnClickListener {
            todoAdapter.deleteDone()
        }

        // note button to get the clicked to-do's desc/ note and send it to new activity
        noteButton.setOnClickListener {
            val todos = todoAdapter.getTodos()
            // if no to-do's are made then the program exits
            if (todos.isNullOrEmpty()) {
                val toast = Toast.makeText(this, "Please make a to-do first!", Toast.LENGTH_LONG)
                toast.show()
                return@setOnClickListener
            }
            val position = todoAdapter.selectedIndex
            val desc = todos[position].desc
            val intent = Intent(this@MainActivity, NotesActivity::class.java)
            intent.putExtra("Description", desc)
            // strikethrough means startActivityForResult is a depriciated method but I didn't care to look up the recommended way
            // (probably not good if I wanted to ever publish this app)
            startActivityForResult(intent, 1)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // before main activity is destroyed it gets or creates the shared preferences file
        val sharedPreferences: SharedPreferences = getSharedPreferences("todo_tasks", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        // it gets the todos list
        val todos = todoAdapter.getTodos()
        // iterates through each to-do in the list and stores the name and desc/note as a string set
        for ((i, ToDoItem) in todos.withIndex()) {
            val strSet = mutableSetOf<String>()
            // 2 strings in the set, but because they will be stored orderless, I put a 1 infront of the title string to identify it
            // in the infinite loop on Creation
            strSet.add("1" + ToDoItem.title)
            strSet.add(ToDoItem.desc)
            // apply the changes for each set
            sharedPreferences.edit().putStringSet("Task $i", strSet).apply()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check the code of the activity result to make sure the result is from the notes activity (not really needed there is only one other activity
        // but I was gonna create another one to be an About page)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // if everything is okay it gets the data which is either the note the user made
                    // or "none" if the user tried to delete the note or didn't do anything
                val strReturned = data?.getStringExtra("Changed Description")

                // gets the todos list again to modify the selected to-do's desc/note to be the returned string
                // I made the string be "none" always instead of null so I didn't need to convert the string so many times
                val position = todoAdapter.selectedIndex
                val todos = todoAdapter.getTodos()
                todos[position].desc = strReturned.toString()
            }
        }
    }

}