package com.bignerdranch.android.alexcaspercpt188finalapp

import android.widget.TimePicker

// Data class for todo item object, has to-do title checkbox status and note (initialized to none so kotlin doesn't worry about null)
data class ToDoItem (var title: String, var isChecked: Boolean = false, var desc: String = "None")
