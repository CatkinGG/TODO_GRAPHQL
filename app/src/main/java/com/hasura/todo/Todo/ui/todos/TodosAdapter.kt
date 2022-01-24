package com.hasura.todo.Todo.ui.todos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasura.todo.GetTodoListQuery
import com.hasura.todo.Todo.R
import kotlinx.android.synthetic.main.item_todo.view.*

open class TodosAdapter(
    private val todosFuncItem: TodosFuncItem
): PagingDataAdapter<GetTodoListQuery.Todo_list, TodoViewHolder>(diffCallback) {

    companion object {
        const val VIEW_TYPE_TODO = 1

        private val diffCallback = object : DiffUtil.ItemCallback<GetTodoListQuery.Todo_list>() {
            override fun areItemsTheSame(
                oldItem: GetTodoListQuery.Todo_list,
                newItem: GetTodoListQuery.Todo_list
            ): Boolean {
                return oldItem.id() == newItem.id()
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: GetTodoListQuery.Todo_list,
                newItem: GetTodoListQuery.Todo_list
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todo, parent, false),
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        getItem(position)?.run {
            holder.onBind(this, todosFuncItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_TODO
    }

    open fun isDataEmpty(): Boolean {
        return itemCount == 0
    }
}

class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val tvTask: TextView = itemView.tv_task
    private val tvAssignee: TextView = itemView.tv_assignee
    private val btnEdit: TextView = itemView.edit
    private val btnDelete: TextView = itemView.delete
    private val tvTime: TextView = itemView.tv_time

    @SuppressLint("SetTextI18n")
    fun onBind(item: GetTodoListQuery.Todo_list, todosFuncItem: TodosFuncItem){
        tvTask.text = item.id().toString() + ": " + item.task()
        tvAssignee.text = "Assignee: " + item.assignee()
        tvTime.text = item.updated_at().toString()
        btnEdit.setOnClickListener { todosFuncItem.onEdit.invoke(item) }
        btnDelete.setOnClickListener { todosFuncItem.onDelete.invoke(item.id()) }
    }
}