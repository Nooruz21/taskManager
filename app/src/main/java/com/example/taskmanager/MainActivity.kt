package com.example.taskmanager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private val list: MutableList<Task.Data?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initClicker()

    }

    private fun initRecyclerView() {
        adapter = TaskAdapter(list)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

    }

    private fun initClicker() {
        binding.fab.setOnClickListener {
            showAddTaskBottomSheet()
        }
        val taskApi = RetrofitService().api
        taskApi.getTask().enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    val task = response.body()
                    if (task != null && task.status == "success") {
                        val taskData = task.data
                        if (taskData != null) {
                            list.clear()
                            list.addAll(taskData)
                            adapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    if (list.isNotEmpty()) {
                        binding.image.visibility = View.GONE
                        binding.text.visibility = View.GONE
                        binding.text2.visibility = View.GONE
                    } else {
                        binding.image.visibility = View.VISIBLE
                        binding.text.visibility = View.VISIBLE
                        binding.text2.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.d("TaskManager", "onFailure: ${t.message}")
            }
        })

    }

    private fun showAddTaskBottomSheet() {
        val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null)
        val editTextTask = bottomSheetView.findViewById<EditText>(R.id.editTextTask)
        val buttonAddTask = bottomSheetView.findViewById<FloatingActionButton>(R.id.buttonAddTask)

        val bottomScreen = BottomSheetDialog(this)
        bottomScreen.setContentView(bottomSheetView)
        bottomScreen.show()

        buttonAddTask.setOnClickListener {
            val taskText = editTextTask.text.toString()
            if (taskText.isNotEmpty()) {
                addTask(taskText)
                bottomScreen.dismiss()
            }
        }


    }

    private fun addTask(taskText: String) {
        val taskApi = RetrofitService().api
        taskApi.updateTask(taskText).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    val task = response.body()
                    if (task != null && task.status == "success") {
                        val getFirstTask = task.data?.get(0)
                        val taskData = getFirstTask
                        if (taskData != null) {
                            list.add(taskData)
                            adapter.notifyItemInserted(list.size - 1)
                        }
                    }
                } else {
                    if (list.isEmpty()) {
                        binding.image.visibility = View.VISIBLE
                        binding.text.visibility = View.VISIBLE
                        binding.text2.visibility = View.VISIBLE
                    } else {
                        binding.image.visibility = View.GONE
                        binding.text.visibility = View.GONE
                        binding.text2.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.d("TaskManager", "onFailure: ${t.message}")
            }
        })
      /*  if (list.isNotEmpty()) {
            binding.image.visibility = View.GONE
            binding.text.visibility = View.GONE
            binding.text2.visibility = View.GONE
        } else {
            binding.image.visibility = View.VISIBLE
            binding.text.visibility = View.VISIBLE
            binding.text2.visibility = View.VISIBLE
        }*/
    }


}
