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
import com.example.taskreminder.databinding.FragmentTodayBinding
import com.example.taskreminder.listeners.TaskListener
import com.example.taskreminder.model.TaskModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodayFragment : Fragment(), TaskListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    private val viewModel =  ViewModelProvider(this,viewModelFactory).get(TaskViewModel.class)

    private lateinit var binding: FragmentTodayBinding
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
        binding = FragmentTodayBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
//        val df = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
        val formattedDate = df.format(c)

        val todayTasks = ArrayList<TaskModel>()
        TaskDatabase.getDatabase(requireContext())
            .taskDao()
            .getTasks().observe(viewLifecycleOwner) { tasks ->

                todayTasks.clear()
                tasks.forEach {
                    if (it.date.contains(formattedDate) && !it.isCompleted) {
                        todayTasks.add(it)
                        binding.todayRecyclerView.visibility = View.VISIBLE
                        binding.textStatus.visibility = View.GONE
                    }
                }
                taskAdapter = TaskAdapter(todayTasks, 1, this)
                binding.todayRecyclerView.adapter = taskAdapter

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