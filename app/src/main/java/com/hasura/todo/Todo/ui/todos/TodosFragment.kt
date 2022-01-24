package com.hasura.todo.Todo.ui.todos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.hasura.todo.Todo.R
import com.hasura.todo.Todo.ui.todos.edit.EditTodosDialogFragment
import com.hasura.todo.Todo.ui.todos.edit.GeneralDialogFuncListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todos.*
import timber.log.Timber

@AndroidEntryPoint
class TodosFragment : Fragment(R.layout.fragment_todos) {
    companion object {
        fun newInstance() = TodosFragment()
    }
    private var isModified = false
    private val viewModel: TodosViewModel by viewModels()

    private val todosFuncItem = TodosFuncItem(
        onEdit = { item ->
            Timber.d("Edit $item")
            val dialog = EditTodosDialogFragment.newInstance(item)
            dialog.dialogFuncListener = GeneralDialogFuncListener(
                onEditConfirmClick = {id, assignee, task ->
                    Timber.d("dialog Edit conform")
                    id?.also {
                        viewModel.updateTodoList(id,assignee, task)
                    }
                },
                onCancelClick = {
                    Timber.d("dialog Edit cancel")
                }
            )
            dialog.show(requireActivity().supportFragmentManager, "EditTodosDialog")
                 },
        onDelete = {id ->
            Timber.d("delete $id")
            viewModel.removeTodoList(id)
        }
    )
    private val todosAdapter by lazy {
        val adapter = TodosAdapter(todosFuncItem)
        val loadStateListener = { loadStatus: CombinedLoadStates ->
            when (loadStatus.refresh) {
                is LoadState.Error -> {
                    layout_refresh.isRefreshing = false
                }
                is LoadState.Loading -> {
                    layout_refresh.isRefreshing = true
                }
                is LoadState.NotLoading -> {
                    layout_refresh.isRefreshing = false
                    if(isModified){
                        isModified = false
                        rv_todo_list.smoothScrollToPosition(0)
                    }
                }
            }

            when (loadStatus.append) {
                is LoadState.Error -> {
                }
                is LoadState.Loading -> {
                }
                is LoadState.NotLoading -> {
                }
            }
        }
        adapter.addLoadStateListener(loadStateListener)
        adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        setupListener()
//        viewModel.getMyTodos()
    }

    fun setupUI() {
        rv_todo_list.also {
            it.setHasFixedSize(true)
            it.adapter = todosAdapter
            it.itemAnimator = null
        }

        layout_refresh.setOnRefreshListener {
            todosAdapter.refresh()
        }
    }

    fun setupObserver() {
        viewModel.getTodoListResult.observe(viewLifecycleOwner, {
            Log.d("catkingg2 Todo", it.toString() )
        })

        viewModel.pagingDataResult.observe(viewLifecycleOwner, {
            todosAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        viewModel.removeTodoResult.observe(viewLifecycleOwner, {
            todosAdapter.refresh()
        })

        viewModel.updateTodoResult.observe(viewLifecycleOwner, {
            isModified = true
            todosAdapter.refresh()
        })

        viewModel.addTodoResult.observe(viewLifecycleOwner, {
            isModified = true
            todosAdapter.refresh()
        })
    }

    fun setupListener() {
        bt_add.setOnClickListener {
            val dialog = EditTodosDialogFragment.newInstance(null)
            dialog.dialogFuncListener = GeneralDialogFuncListener(
                onInsertConfirmClick = {assignee, task ->
                    Timber.d("dialog add conform")
                    viewModel.addTodoList(assignee, task)
                },
                onCancelClick = {
                    Timber.d("dialog add cancel")
                }
            )
            dialog.show(requireActivity().supportFragmentManager, "EditTodosDialog")
        }
    }
}