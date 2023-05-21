package com.example.taskmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.ItemTaskBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskAdapter(private val list: MutableList<Task.Data?>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(task: Task.Data) {
            binding.itemNameTextView.text = task.text

            binding.radioButton.setOnCheckedChangeListener(null)

            binding.radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val taskToDelete = list[position]
                        taskToDelete?.id?.let { deleteTask(it) }
                    }
                }
            }
        }

        private fun deleteTask(it: Int) {
            val taskApi = RetrofitService().api
            taskApi.deleteTask(it).enqueue(object : Callback<Task> {
                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    if (response.isSuccessful) {
                        val position = list.indexOfFirst { it?.id == itemCount }
                        if (position != -1) {
                            list.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    } else {
                        //обработка ошибок
                    }
                }

                override fun onFailure(call: Call<Task>, t: Throwable) {
                }
            })
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        list[position]?.let { holder.onBind(it) }
    }

    override fun getItemCount(): Int = list.size


}