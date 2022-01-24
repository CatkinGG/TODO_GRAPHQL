package com.hasura.todo.Todo.ui.todos

import androidx.paging.PagingSource
import com.hasura.todo.GetTodoListQuery
import com.hasura.todo.Todo.model.manager.RepositoryManager
import timber.log.Timber

class TodosPagingSource(
    private val repositoryManager: RepositoryManager
) : PagingSource<Int, GetTodoListQuery.Todo_list>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetTodoListQuery.Todo_list> {
        return try {
            val page = params.key ?: 0

            val result = repositoryManager.todoRepository.getTodoList(10, page)
            val data = result?.data?.todo_list()

            val nextKey = when {
                data?.size ?: 0 >= 10 -> page + 1
                else -> null
            }

            return LoadResult.Page(
                data = data ?: arrayListOf(),
                prevKey = null,
                nextKey = nextKey
            )

        } catch (exception: Exception) {
            Timber.e(exception)
            LoadResult.Error(exception)
        }
    }
}