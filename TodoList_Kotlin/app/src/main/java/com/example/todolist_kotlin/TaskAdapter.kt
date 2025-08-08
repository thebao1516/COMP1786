package com.example.todolist_kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist_kotlin.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onDeleteClicked: (Int) -> Unit,
    private val onEditClicked: (Int) -> Unit,
    private val onCompletedChanged: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) onDeleteClicked(position)
            }
            binding.taskName.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) onEditClicked(position)
            }
            binding.taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) onCompletedChanged(position, isChecked)
            }
        }

        fun bind(task: Task) {
            binding.taskName.text = task.name
            binding.taskCheckbox.isChecked = task.completed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size
}