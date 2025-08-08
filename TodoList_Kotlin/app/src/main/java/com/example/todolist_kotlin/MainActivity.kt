package com.example.todolist_kotlin

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist_kotlin.databinding.DialogEditTaskBinding
import com.example.todolist_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tasks: MutableList<Task>
    private lateinit var adapter: TaskAdapter
    private lateinit var dbHelper: TaskDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = TaskDatabaseHelper(this)
        tasks = dbHelper.getAllTasks().toMutableList()

        adapter = TaskAdapter(tasks,
            onDeleteClicked = { position ->
                val task = tasks[position]
                dbHelper.deleteTask(task.id)
                tasks.removeAt(position)
                adapter.notifyItemRemoved(position)
            },
            onEditClicked = { position ->
                showTaskDialog("Edit Task", tasks[position].name) { updatedName ->
                    val task = tasks[position]
                    task.name = updatedName
                    dbHelper.updateTask(task.id, task.name, task.completed)
                    adapter.notifyItemChanged(position)
                }
            },
            onCompletedChanged = { position, isCompleted ->
                val task = tasks[position]
                task.completed = isCompleted
                dbHelper.updateTask(task.id, task.name, task.completed)
            }
        )
        binding.taskList.layoutManager = LinearLayoutManager(this)
        binding.taskList.adapter = adapter

        binding.addButton.setOnClickListener {
            showTaskDialog("Add Task") { newName ->
                val id = dbHelper.insertTask(newName, false)
                val newTask = Task(id, newName, false)
                tasks.add(newTask)
                adapter.notifyItemInserted(tasks.size - 1)
            }
        }
    }

    private fun showTaskDialog(title: String, initialText: String? = null, onOk: (String) -> Unit) {
        val dialogBinding = DialogEditTaskBinding.inflate(layoutInflater)
        initialText?.let { dialogBinding.taskEditText.setText(it) }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                val text = dialogBinding.taskEditText.text.toString()
                if (text.isNotEmpty()) {
                    onOk(text)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}