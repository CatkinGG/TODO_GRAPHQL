package com.hasura.todo.Todo.ui.todos

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.apollographql.apollo.api.Response
import com.hasura.todo.*
import com.hasura.todo.Todo.model.manager.RepositoryManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

class TodosViewModel @ViewModelInject constructor(
    private val repositoryManager: RepositoryManager,
) : ViewModel() {
    private val _getTodoListResult = MutableLiveData<GetAllTodoListQuery.Data?>()
    val getTodoListResult: LiveData<GetAllTodoListQuery.Data?> = _getTodoListResult

    private val _addTodoResult = MutableLiveData<Response<AddTodoListMutation.Data>>()
    val addTodoResult: LiveData<Response<AddTodoListMutation.Data>> = _addTodoResult

    private val _updateTodoResult = MutableLiveData<Response<UpdateTodoListMutation.Data>>()
    val updateTodoResult: LiveData<Response<UpdateTodoListMutation.Data>> = _updateTodoResult

    private val _removeTodoResult = MutableLiveData<Response<RemoveTodoListMutation.Data>>()
    val removeTodoResult: LiveData<Response<RemoveTodoListMutation.Data>> = _removeTodoResult

    private val _pagingDataResult = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TodosPagingSource(repositoryManager) }
    ).flow
        .cachedIn(viewModelScope).asLiveData()
        .let { it as MutableLiveData<PagingData<GetTodoListQuery.Todo_list>> }
    val pagingDataResult: LiveData<PagingData<GetTodoListQuery.Todo_list>> = _pagingDataResult

    fun getMyTodos() {
        viewModelScope.launch {
            repositoryManager.todoRepository.getTodoList()
                .catch { e -> Timber.d("catkingg e $e") }
                .collect {
                    Timber.d("catkingg $it")
                    _getTodoListResult.postValue(it)
                }
        }
    }

    fun removeTodoList(id: Int) {
        viewModelScope.launch {
            repositoryManager.todoRepository.removeTodoList(id)
                .catch { e -> Timber.d("$e") }
                .collect {
                    Timber.d("catkingg $it")
                    _removeTodoResult.postValue(it)
                }
        }
    }

    fun addTodoList(assignee: String, task: String) {
        viewModelScope.launch {
            repositoryManager.todoRepository.addTodoList(assignee, task)
                .catch { e -> Timber.d("$e") }
                .collect {
                    Timber.d("$it")
                    _addTodoResult.postValue(it)
                }
        }
    }

    fun updateTodoList(id: Int, assignee: String, task: String) {
        viewModelScope.launch {
            repositoryManager.todoRepository.updateTodoList(id, assignee, task)
                .catch { e -> Timber.d("$e") }
                .collect {
                    Timber.d("$it")
                    _updateTodoResult.postValue(it)
                }
        }
    }

}