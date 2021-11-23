package com.example.taskreminder.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.taskreminder.CreateTaskActivity
import com.example.taskreminder.adapter.TaskAdapter
import com.example.taskreminder.database.TaskDatabase
import com.example.taskreminder.databinding.FragmentCompletedBinding
import com.example.taskreminder.listeners.TaskListener
import com.example.taskreminder.model.TaskModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CompletedFragment : Fragment(), TaskListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentCompletedBinding
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val completedTasks = ArrayList<TaskModel>()

        TaskDatabase.getDatabase(requireContext())
            .taskDao()
            .getTasks().observe(viewLifecycleOwner) { tasks ->

                completedTasks.clear()
                tasks.forEach {
                    if (it.isCompleted) {
                        completedTasks.add(it)
                    }
                }
                taskAdapter = TaskAdapter(completedTasks,3, this)
                binding.completedRecyclerView.adapter = taskAdapter

            }
    }


    override fun onTaskClicked(task: TaskModel, position: Int) {


        val intent = Intent(requireContext(), CreateTaskActivity::class.java)
        intent.putExtra("isViewOrUpdate", true)
//        intent.putExtra("title", task.title)
//        intent.putExtra("description", task.description)
//        intent.putExtra("category", task.category)
//        intent.putExtra("date", task.date)
        intent.putExtra("note", task)
        startActivity(intent)
    }
}