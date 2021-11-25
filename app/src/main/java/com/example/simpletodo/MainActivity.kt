package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import org.apache.commons.io.FileUtils
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //1. Remove item from list
                listOfTasks.removeAt(position)
                //2. Notify adapter that something has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        loadItems()

        //look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sampler user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        //attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        //set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up button and input field so that the user can enter a task.
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        findViewById<Button>(R.id.button).setOnClickListener {
            //1. Grab text the user has entered into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()
            //2. Add the string to our list of tasks
            listOfTasks.add(userInputtedTask)
            adapter.notifyItemInserted(listOfTasks.size - 1)
            //3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that the user has inputted
    // Save data by writing and reading from a file
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    // Save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}