package com.example.taskreminder.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskreminder.R
import com.example.taskreminder.databinding.ItemTaskLayoutBinding
import com.example.taskreminder.listeners.TaskListener
import com.example.taskreminder.model.TaskModel

class TaskAdapter(private val todayTasks: ArrayList<TaskModel>, private val type: Int, private val taskListener: TaskListener) :
    RecyclerView.Adapter<TaskAdapter.TaskVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskVH {
        val binding =
            ItemTaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskVH(binding, type)
    }

    override fun onBindViewHolder(holder: TaskVH, position: Int) {
        holder.bindData(todayTasks[position], taskListener, position)


    }

    override fun getItemCount(): Int = todayTasks.size

    class TaskVH(private val binding: ItemTaskLayoutBinding, private val type: Int) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(task: TaskModel, taskListener: TaskListener, position: Int) {
            if (type == 3) {
                binding.imageStatus.visibility = View.VISIBLE
                binding.imageStatus.setImageResource(R.drawable.ic_completed_task)
            } else if (type == 2) {
                binding.imageStatus.visibility = View.VISIBLE
                binding.imageStatus.setImageResource(R.drawable.ic_pending)
            }
            binding.textTitle.text = task.title
            binding.textSubtitle.text = task.description
            binding.textDateTime.text = task.date

            val gradientDrawable = binding.viewSubtitleIndicator.background as GradientDrawable
            gradientDrawable.setColor(Color.parseColor(task.category))


            binding.root.setOnClickListener {
                taskListener.onTaskClicked(task, position)
            }

        }
    }

}