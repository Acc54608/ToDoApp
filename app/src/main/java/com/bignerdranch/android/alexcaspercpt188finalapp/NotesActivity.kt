package com.bignerdranch.android.alexcaspercpt188finalapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        // creates a val to be used in place of null and gets the data passed from the main activity
        // (the selected to-do's desc/note
        val noText = "None"
        val desc = intent.getStringExtra("Description")
        val saveButton = findViewById<Button>(R.id.save_button)
        // sets the edit text's text to the desc/note
        etNote.setText(desc)
        // on button click, if the edit text is empty then it's text is changed to "none" from above
        saveButton.setOnClickListener {
            if (etNote.text.isNullOrEmpty()) {
                etNote.setText(noText)
            }
            // otherwise this string val is set to the text of the edit text
            val descChanged = etNote.text.toString()

            // the string is sent back to the Main Activity to be used in the
            // onActivityResult function
            val intent = Intent()
            intent.putExtra("Changed Description", descChanged)
            setResult(RESULT_OK, intent)
            finish()
        }

    }
}